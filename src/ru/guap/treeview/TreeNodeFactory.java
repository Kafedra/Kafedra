package ru.guap.treeview;

import ru.guap.dao.DBManager;

import com.jsptree.bean.Node;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;

public class TreeNodeFactory {
		
	public static Hashtable<String, String> loadKindRenamingTable;
	static {
		loadKindRenamingTable = new Hashtable<>();
		
		loadKindRenamingTable.put("экз", "Экзамен");
		loadKindRenamingTable.put("зач", "Зачёт");
		loadKindRenamingTable.put("лекц", "Лекция");
		loadKindRenamingTable.put("лаб", "Лабораторная");
		loadKindRenamingTable.put("практ", "Практика");
		loadKindRenamingTable.put("экз.конс.", "Консультация");
		loadKindRenamingTable.put("контр.", "Контрольная");
		loadKindRenamingTable.put("КП", "Курсовой проект");
	}
	
	static Node rootAuthum = null;
	static Node rootSpring = null;
	
	static PreparedStatement selectDiscNamesAuthum;
	static String selectDiscNamesAuthumSql = "SELECT DISTINCT NameDisc FROM kafedra.kaf43 WHERE ((mod(Nsem, 2) = 0) AND (load_id = ?))";
	
	static PreparedStatement selectDiscNamesSpring;
	static String selectDiscNamesSpringSql = "SELECT DISTINCT NameDisc FROM kafedra.kaf43 WHERE ((mod(Nsem, 2) != 0) AND (load_id = ?))";
	
	static PreparedStatement selectGroupsAndLoadsAuthum;
	static String selectGroupsAndLoadsAuthumSql = "SELECT id, Nstream, `Group`, KindLoad, teachers_id FROM kafedra.kaf43 WHERE (mod(Nsem, 2) = 0) AND (NameDisc = ? AND load_id=?)";
	
	static PreparedStatement selectGroupsAndLoadsSpring;
	static String selectGroupsAndLoadsSpringSql = "SELECT id, Nstream, `Group`, KindLoad, teachers_id FROM kafedra.kaf43 WHERE (mod(Nsem, 2) != 0) AND (NameDisc = ? AND load_id=?)";
	
	public static Node getRootNode(boolean isAuthum) throws SQLException {
		if (isAuthum) {
			//if (rootAuthum == null) { // without caching
				rootAuthum = getTreeFromDb(isAuthum, 2);
			//}
			
			return rootAuthum;
		} else {
			//if (rootSpring == null) { // without caching
				rootSpring = getTreeFromDb(isAuthum, 2);
			//}
			
			return rootSpring;
		}
	}
	
	private static Node getTreeFromDb(boolean isAuthum, int version) throws SQLException {
		Connection cnn = DBManager.getInstance().getConnection();
		
		Node root = new Node("root");
		root.setIsSelected(0);
		root.setErrorDescription("");
		
		// Get disciplines names
		ArrayList<String> discNames = new ArrayList<>();
		if (isAuthum) {
			if (selectDiscNamesAuthum == null) {
				selectDiscNamesAuthum = cnn.prepareStatement(selectDiscNamesAuthumSql);
			}
			
			if (selectGroupsAndLoadsAuthum == null) {
				selectGroupsAndLoadsAuthum = cnn.prepareStatement(selectGroupsAndLoadsAuthumSql);
			}
			
			getDiscNames(selectDiscNamesAuthum, discNames, version);
		} else {
			if (selectDiscNamesSpring == null) {
				selectDiscNamesSpring = cnn.prepareStatement(selectDiscNamesSpringSql);
			}
			
			if (selectGroupsAndLoadsSpring == null) {
				selectGroupsAndLoadsSpring = cnn.prepareStatement(selectGroupsAndLoadsSpringSql);
			}
			
			getDiscNames(selectDiscNamesSpring, discNames, version);			
		}
		
		// Add disciplines in tree
		for (String discName : discNames) {
			Node node = new Node(new Integer(discName.hashCode()).toString(), discName);
			node.setIsSelected(0);
			node.setErrorDescription("");
			
			setGroupsAndLoadsToDisc(node, cnn, isAuthum, version);
			
			root.addChildNode(node);
		}
		
		return root;
	}
	
	private static void setGroupsAndLoadsToDisc(Node disc, Connection cnn, boolean isAuthum, int version) throws SQLException {
		Hashtable<Integer, String> streams = new Hashtable<>(); // Stream data by stream id
		
		PreparedStatement ps;
		if (isAuthum) {
			ps = cnn.prepareStatement("SELECT DISTINCT Nstream FROM kafedra.kaf43 WHERE (mod(Nsem, 2) = 0) AND (NameDisc=? AND Nstream != 0 AND load_id=?)");
		} else {
			ps = cnn.prepareStatement("SELECT DISTINCT Nstream FROM kafedra.kaf43 WHERE (mod(Nsem, 2) != 0) AND (NameDisc=? AND Nstream != 0 AND load_id=?)");
		}
		
		ps.setString(1, disc.getNodeName());
		ps.setInt(2, version);
		
		ResultSet res = ps.executeQuery();
		
		// Found stream 
		if (res.next()) {
			int nStream = res.getInt(1);
			ArrayList<String> streamGroups = getGroupListForStream(cnn, isAuthum, nStream, disc.getNodeName(), version);

			StringBuilder streamString = new StringBuilder("Поток: ");

			for (int i = 0; i < streamGroups.size(); i++) {
				streamString.append(streamGroups.get(i));
				
				if (i + 1 != streamGroups.size()) {
					streamString.append(", ");
				}
			}
			
			Node streamNode = new Node(new Integer(nStream).toString(), streamString.toString());
			disc.addChildNode(streamNode);
			
			Hashtable<String, Node> loadNodes = new Hashtable<>();
			
			for (String group : streamGroups) {
				ArrayList<String> loadKinds = getLoadKindsForDiscAndGroup(cnn, isAuthum, disc.getNodeName(), group, version);
				
				for (String kind : loadKinds) {
					if (loadNodes.containsKey(kind)) {
						int loadID = getLoadIdByGroupKindDisc(cnn, isAuthum, group, kind, disc.getNodeName(), version);
						
						if (loadID != 0) {
							Node groupLeaf = new Node(new Integer(loadID).toString(), group);
							groupLeaf.setLoadNode(true);
							
							loadNodes.get(kind).addChildNode(groupLeaf);
						} else {
							System.out.println("LoadID is ZERO! for " + group + " " + kind + " " + disc.getNodeName());
						}
					} else {
						int loadID = getLoadIdByGroupKindDisc(cnn, isAuthum, group, kind, disc.getNodeName(), version);
						
						if (loadID != 0) {
							Node loadKindNode = new Node("0", loadKindRenamingTable.get(kind));
							loadNodes.put(kind, loadKindNode);
							
							Node groupLeaf = new Node(new Integer(loadID).toString(), group);
							groupLeaf.setLoadNode(true);
							loadNodes.get(kind).addChildNode(groupLeaf);
						} else {
							System.out.println("LoadID is ZERO! for " + group + " " + kind + " " + disc.getNodeName());
						}						
					}
				}
			}
			
			for (String k : loadNodes.keySet()) {
				streamNode.addChildNode(loadNodes.get(k));
			}
		} else { // Is not stream
			ArrayList<String> discGroups = getGroupListForDisc(cnn, isAuthum, disc.getNodeName(), version);
			
			for (String group : discGroups) {
				Node groupNode = new Node("0", group);
				
				ArrayList<String> loadKinds = getLoadKindsForDiscAndGroup(cnn, isAuthum, disc.getNodeName(), group, version);
				
				for (String kind : loadKinds) {
					int loadID = getLoadIdByGroupKindDisc(cnn, isAuthum, group, kind, disc.getNodeName(), version);
					
					Node kindNode = new Node(new Integer(loadID).toString(), loadKindRenamingTable.get(kind));
					
					groupNode.addChildNode(kindNode);
				}
				
				disc.addChildNode(groupNode);
			}
		}
	}
	
	private static int getLoadIdByGroupKindDisc(Connection cnn, boolean isAuthum, String group, String kind, String discName, int version) throws SQLException {
		int result = 0;
		
		PreparedStatement ps;
		if (isAuthum) {
			ps = cnn.prepareStatement("SELECT id FROM kafedra.kaf43 WHERE (mod(Nsem, 2) = 0) AND (NameDisc = ? AND `Group` = ? AND KindLoad = ? AND load_id = ?)");
		} else {
			ps = cnn.prepareStatement("SELECT id KindLoad FROM kafedra.kaf43 WHERE (mod(Nsem, 2) != 0) AND (NameDisc = ? AND `Group` = ? AND KindLoad = ? AND load_id = ?)");
		}

		ps.setString(1, discName);
		ps.setString(2, group);
		ps.setString(3, kind);
		ps.setInt(4, version);
		
		ResultSet res = ps.executeQuery();
		
		while (res.next()) {
			result = res.getInt(1);
		}
		
		return result;		
	}
	
	private static ArrayList<String> getLoadKindsForDiscAndGroup(Connection cnn, boolean isAuthum, String discName, String group, int version) throws SQLException {
		ArrayList<String> result = new ArrayList<String>();
		
		PreparedStatement ps;
		if (isAuthum) {
			ps = cnn.prepareStatement("SELECT DISTINCT KindLoad FROM kafedra.kaf43 WHERE (mod(Nsem, 2) = 0) AND (NameDisc = ? AND `Group` = ? AND load_id = ?)");
		} else {
			ps = cnn.prepareStatement("SELECT DISTINCT KindLoad FROM kafedra.kaf43 WHERE (mod(Nsem, 2) != 0) AND (NameDisc = ? AND `Group` = ? AND load_id = ?)");
		}

		ps.setString(1, discName);
		ps.setString(2, group);
		ps.setInt(3, version);		
		
		ResultSet res = ps.executeQuery();
		
		while (res.next()) {
			String kindLoad = res.getString(1);
			
			result.add(kindLoad);
		}
		
		return result;
	}
	
	private static ArrayList<String> getGroupListForDisc(Connection cnn, boolean isAuthum, String discName, int version) throws SQLException {
		PreparedStatement ps;
		if (isAuthum) {
			ps = cnn.prepareStatement("SELECT DISTINCT `Group` FROM kafedra.kaf43 WHERE (mod(Nsem, 2) = 0) AND (NameDisc = ? AND load_id = ?)");
		} else {
			ps = cnn.prepareStatement("SELECT DISTINCT `Group` FROM kafedra.kaf43 WHERE (mod(Nsem, 2) != 0) AND (NameDisc = ? AND load_id = ?)");
		}

		ps.setString(1, discName);
		ps.setInt(2, version);
		
		ps.execute();
	    ResultSet res = ps.getResultSet();
	    
	    ArrayList<String> groups = new ArrayList<>();
	    
	    while (res.next()) {
	    	groups.add(res.getString(1));
	    }
	    
	    return groups;
	}

	
	private static ArrayList<String> getGroupListForStream(Connection cnn, boolean isAuthum, int nStream, String discName, int version) throws SQLException {
		PreparedStatement ps;
		if (isAuthum) {
			ps = cnn.prepareStatement("SELECT DISTINCT `Group` FROM kafedra.kaf43 WHERE (mod(Nsem, 2) = 0) AND (NameDisc = ? AND NStream = ? AND load_id = ?)");
		} else {
			ps = cnn.prepareStatement("SELECT DISTINCT `Group` FROM kafedra.kaf43 WHERE (mod(Nsem, 2) != 0) AND (NameDisc = ? AND NStream = ? AND load_id = ?)");
		}

		ps.setString(1, discName);
		ps.setInt(2, nStream);
		ps.setInt(3, version);
		
		ps.execute();
	    ResultSet res = ps.getResultSet();
	    
	    ArrayList<String> groups = new ArrayList<>();
	    
	    while (res.next()) {
	    	groups.add(res.getString(1));
	    }
	    
	    return groups;
	}
	
	private static void getDiscNames(PreparedStatement ps, ArrayList<String> discNames, int version) throws SQLException {
		ps.setInt(1, version);
		
		ps.execute();
	    ResultSet res = ps.getResultSet();
	    
	    while (res.next()) {
	    	discNames.add(res.getString(1));
	    }
	}
}
