package ru.guap.treeview;

import ru.guap.dao.DBManager;

import com.jsptree.bean.Node;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

public class TreeNodeFactory {
		
	public static Hashtable<String, String> loadKindRenamingTable;
	static {
		loadKindRenamingTable = new Hashtable<>();
		
		loadKindRenamingTable.put("лекц", "Лекция");
		loadKindRenamingTable.put("практ", "Практика");
		loadKindRenamingTable.put("лаб", "Лабораторная");
		loadKindRenamingTable.put("КР", "Курсовая работа");
		loadKindRenamingTable.put("КП", "Курсовой проект");
		loadKindRenamingTable.put("контр.", "Контрольная");
		loadKindRenamingTable.put("зач", "Зачёт");
		loadKindRenamingTable.put("экз.конс.", "Консультация");
		loadKindRenamingTable.put("экз", "Экзамен");
	}
	
	static Node rootAuthum = null;
	static Node rootSpring = null;
	
	static PreparedStatement selectDiscNamesAuthum;
	static String selectDiscNamesAuthumSql = "SELECT DISTINCT NameDisc FROM kafedra.kaf43 WHERE ((mod(Nsem, 2) != 0) AND (load_id = ?))";
	
	static PreparedStatement selectDiscNamesSpring;
	static String selectDiscNamesSpringSql = "SELECT DISTINCT NameDisc FROM kafedra.kaf43 WHERE ((mod(Nsem, 2) = 0) AND (load_id = ?))";
	
	static PreparedStatement selectGroupsAuthum;
	static String selectGroupsAuthumSql = "SELECT `Group`, NStream, KindLoad, id, teachers_id FROM kafedra.kaf43 WHERE (mod(Nsem, 2) != 0) AND (load_id = ?) AND (NameDisc = ?)";

	static PreparedStatement selectGroupsSpring;
	static String selectGroupsSpringSql = "SELECT `Group`, NStream, KindLoad, id, teachers_id FROM kafedra.kaf43 WHERE (mod(Nsem, 2) = 0) AND (load_id = ?) AND (NameDisc = ?)";	
	
	static PreparedStatement appointmentCheck;
	static final String appointmentCheckSql = "SELECT teachers_id FROM kafedra.kaf43 WHERE (id = ?) AND (load_id = ?)";

	public static final int LOAD_VERSION = 1;
	
	public static Node getRootNode(boolean isAutumn) throws SQLException {
		if (isAutumn) {
			//if (rootAuthum == null) { // without caching
				rootAuthum = getTreeFromDb(isAutumn, LOAD_VERSION);
			//}
			
			return rootAuthum;
		} else {
			//if (rootSpring == null) { // without caching
				rootSpring = getTreeFromDb(isAutumn, LOAD_VERSION);
			//}
			
			return rootSpring;
		}
	}
	
	private static Node getTreeFromDb(boolean isAutumn, int version) throws SQLException {
		Connection cnn = DBManager.getInstance().getConnection();
		
		Node root = new Node("root");
		root.setIsSelected(0);
		root.setErrorDescription("");
		
		// Get disciplines names
		ArrayList<String> discNames = new ArrayList<>();
		if (isAutumn) {
			if (selectDiscNamesAuthum == null) {
				selectDiscNamesAuthum = cnn.prepareStatement(selectDiscNamesAuthumSql);
			}
			
			getDiscNames(selectDiscNamesAuthum, discNames, version);
		} else {
			if (selectDiscNamesSpring == null) {
				selectDiscNamesSpring = cnn.prepareStatement(selectDiscNamesSpringSql);
			}
						
			getDiscNames(selectDiscNamesSpring, discNames, version);			
		}
		
		// Add disciplines in tree
		for (String discName : discNames) {
			
			
			Node node = new Node(new Integer(discName.hashCode()).toString(), discName);
			node.setIsSelected(0);
			node.setErrorDescription("");
			
			ArrayList<GroupLoadItem> groupsList = getGroupsForDiscipline(discName, version, isAutumn);
			
			GroupStream zeroStream = new GroupStream(true, 0);
			HashMap<Integer, GroupStream> nonZeroStreams = new HashMap<>();
			
			for (GroupLoadItem item : groupsList) {
				if (item.streamId == 0) {
					zeroStream.addGroupItem(item);
				} else {
					// Try to find stream if already added
					GroupStream stream = nonZeroStreams.get(item.streamId);
					
					// If stream already added, then add item to it
					if (stream != null) {
						stream.addGroupItem(item);
					} else {
						// If not, create a new stream, save it and add item to new stream
						stream = new GroupStream(false, item.streamId);
						stream.addGroupItem(item);
						
						nonZeroStreams.put(item.streamId, stream);
					}
				}
			}
			
			// Transform streams to nodes
			for (GroupStream stream : nonZeroStreams.values()) {
				addStreamToDiscNode(stream, node);
			}
			
			addStreamToDiscNode(zeroStream, node);
			
			root.addChildNode(node);
		}
		
		return root;
	}

	private static void addStreamToDiscNode(GroupStream stream, Node discNode) {
		Node streamNode = new Node(new Integer(stream.getId()).toString(), stream.toString());
		streamNode.setIsSelected(0);
		streamNode.setAppointed(false);
		streamNode.setLoadNode(false);
		
		HashMap<String, Node> loadKindsNodes = new HashMap<>();
		
		for (GroupLoadItem i : stream.getItems()) {
			Node groupLoadNode = new Node(new Integer(i.id).toString(), i.name);
			groupLoadNode.setLoadNode(true);
			groupLoadNode.setAppointed(i.isAppointed);
			groupLoadNode.setIsSelected(0);
			
			Node loadKindNode = loadKindsNodes.get(i.kindLoad);
			
			if (loadKindNode == null) {
				loadKindNode = new Node("0", loadKindRenamingTable.get(i.kindLoad));
				loadKindsNodes.put(i.kindLoad, loadKindNode);
				
				streamNode.addChildNode(loadKindNode);
			}
			
			loadKindNode.addChildNode(groupLoadNode);
		}
		
		discNode.addChildNode(streamNode);
	}
	
	private static ArrayList<GroupLoadItem> getGroupsForDiscipline(String discName, int version, boolean isAuthumn) throws SQLException {
		ArrayList<GroupLoadItem> result = new ArrayList<>();
		
		Connection cnn = DBManager.getInstance().getConnection();
		
		PreparedStatement ps;
		if (isAuthumn) {
			ps = selectGroupsAuthum;
			
			if (ps == null) {
				ps = cnn.prepareStatement(selectGroupsAuthumSql);
			}
		} else {
			ps = selectGroupsSpring;
			
			if (ps == null) {
				ps = cnn.prepareStatement(selectGroupsSpringSql);
			}
		}
		
		ps.setInt(1, version);
		ps.setString(2, discName);
		
		ResultSet rs = ps.executeQuery();
		
		while (rs.next()) {
			result.add(new GroupLoadItem(rs.getString(1), rs.getInt(2), rs.getString(3), rs.getInt(4), rs.getInt(5) != 0));
		}
		
		return result;		
	}
	
	private static void getDiscNames(PreparedStatement ps, ArrayList<String> discNames, int version) throws SQLException {
		ps.setInt(1, version);
		
		ps.execute();
	    ResultSet res = ps.getResultSet();
	    
	    while (res.next()) {
	    	discNames.add(res.getString(1));
	    }
	}
	
	public static String renameLoadKind(String old) {
		return loadKindRenamingTable.get(old);
	}
}
