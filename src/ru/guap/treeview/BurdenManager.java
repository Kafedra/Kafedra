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
import java.util.LinkedList;

public class BurdenManager {

	private HashMap<Integer, GroupLoadItem> items;
	private LinkedList<Discipline> discsListAutumn;
	private LinkedList<Discipline> discsListSpring;
	
	private static BurdenManager instance;

	private BurdenManager() {
		this.items = new HashMap<>();
		this.discsListAutumn = new LinkedList<>();
		this.discsListSpring = new LinkedList<>();
		
		/*try {
			getRootNode(true);
			getRootNode(false);
		} catch (SQLException e) {
			e.printStackTrace();
		}*/
	}

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
	static String selectDiscNamesAuthumSql = "SELECT DISTINCT NameDisc, CodeDisc FROM kafedra.kaf43 WHERE ((mod(Nsem, 2) != 0) AND (load_id = ?))";

	static PreparedStatement selectDiscNamesSpring;
	static String selectDiscNamesSpringSql = "SELECT DISTINCT NameDisc, CodeDisc FROM kafedra.kaf43 WHERE ((mod(Nsem, 2) = 0) AND (load_id = ?))";

	static PreparedStatement selectGroupsAuthum;
	static String selectGroupsAuthumSql = "SELECT `Group`, NStream, KindLoad, id, teachers_id, ValueEP, ValueG, ValueCO, ValueEP, NameDisc FROM kafedra.kaf43 WHERE (mod(Nsem, 2) != 0) AND (load_id = ?) AND (NameDisc = ?)";

	static PreparedStatement selectGroupsSpring;
	static String selectGroupsSpringSql = "SELECT `Group`, NStream, KindLoad, id, teachers_id, ValueEP, ValueG, ValueCO, ValueEP, NameDisc FROM kafedra.kaf43 WHERE (mod(Nsem, 2) = 0) AND (load_id = ?) AND (NameDisc = ?)";	

	static PreparedStatement appointmentCheck;
	static final String appointmentCheckSql = "SELECT teachers_id FROM kafedra.kaf43 WHERE (id = ?) AND (load_id = ?)";

	public static final int LOAD_VERSION = 1;

	public synchronized static BurdenManager getInstance() {
		if (instance == null) {
			instance = new BurdenManager();
		}

		return instance;
	}

	public GroupStream getStreamForDiscAndId(int streamId, int discId) {
		LinkedList<Discipline> discs = this.getDiscsById(discId, true);
		
		for (Discipline disc : discs) {
			System.out.println("[Au] Found disc " + disc.name + " with id " + discId);
			GroupStream stream = disc.getSteams().get(streamId);
			
			if (stream != null) {
				return stream;
			}
		}
		
		discs = this.getDiscsById(discId, false);
		for (Discipline disc : discs) {
			System.out.println("[Sp] Found disc " + disc.name + " with id " + discId);
			GroupStream stream = disc.getSteams().get(streamId);
			
			if (stream != null) {
				return stream;
			}
		}
		
		return null;
	}
	
	public GroupLoadItem getLoadItemById(int id) {
		return this.items.get(id);
	}
	
	public LinkedList<Discipline> getDiscsById(int discId, boolean isAutumn) {	
		LinkedList<Discipline> list = (isAutumn) ? discsListAutumn : discsListSpring;
		LinkedList<Discipline> res = new LinkedList<>();
	
		System.out.println("Looking for " + discId + " in list of " + list.size() + " discs");
		for (Discipline disc : list) {
			if (disc.id == discId) {
				System.out.println("[>] Found disc " + disc.name + " with id " + discId);
				
				res.add(disc);
			}
		}
		
		return res;
	}

	public Node getRootNode(boolean isAutumn) throws SQLException {
		if (isAutumn) {
			//if (rootAuthum == null) {
				rootAuthum = getTreeFromDb(true, LOAD_VERSION);
			//}

			return rootAuthum;
		} else {
			//if (rootSpring == null) {
				rootSpring = getTreeFromDb(false, LOAD_VERSION);
			//}

			return rootSpring;
		}
	}

	private Node getTreeFromDb(boolean isAutumn, int version) throws SQLException {
		Connection cnn = DBManager.getInstance().getConnection();

		Node root = new Node("root");
		root.setIsSelected(0);
		root.setErrorDescription("");

		// Get disciplines names
		ArrayList<Discipline> discs = new ArrayList<>();
		if (isAutumn) {
			if (selectDiscNamesAuthum == null) {
				selectDiscNamesAuthum = cnn.prepareStatement(selectDiscNamesAuthumSql);
			}

			getDiscNames(selectDiscNamesAuthum, discs, version);
		} else {
			if (selectDiscNamesSpring == null) {
				selectDiscNamesSpring = cnn.prepareStatement(selectDiscNamesSpringSql);
			}

			getDiscNames(selectDiscNamesSpring, discs, version);			
		}

		HashMap<Integer, Discipline> discByCode = new HashMap<>();

		// Add disciplines in tree
		for (Discipline disc: discs) {
			if (!discByCode.containsKey(disc.id)) {
				discByCode.put(disc.id, disc);
			}

			LinkedList<GroupLoadItem> groupsList = getGroupsForDiscipline(disc.name, version, isAutumn);

			GroupStream zeroStream = new GroupStream(true, 0);
			HashMap<Integer, GroupStream> nonZeroStreams = new HashMap<>();

			for (GroupLoadItem item : groupsList) {
				if (item.getStreamId() == 0) {
					zeroStream.addGroupItem(item);
				} else {
					// Try to find stream if already added
					GroupStream stream = null;
					boolean isLecture = item.getKindLoad().equals("лекц");
					if (isLecture) {
						stream = discByCode.get(disc.id).getSteams().get(item.getStreamId());
					} else {
						stream = disc.getSteams().get(item.getStreamId());
					}

					// If stream already added, then add item to it
					if (stream != null) {
						stream.addGroupItem(item);
					} else {
						// If not, create a new stream, save it and add item to new stream
						stream = new GroupStream(false, item.getStreamId());
						stream.addGroupItem(item);

						if (isLecture) {
							stream.setMultiGroup(true);
							discByCode.get(disc.id).addStream(stream);
						} else {
							disc.addStream(stream);
						}
					}
				}
			}

			disc.setZeroStream(zeroStream);
		}

		for (Discipline disc : discs) {
			Node discNode = new Node(new Integer(disc.id).toString(), disc.name);
			discNode.setIsSelected(0);
			discNode.setErrorDescription("");			

			// Transform streams to nodes
			for (GroupStream stream : disc.getSteams().values()) {
				addStreamToDisc(stream, disc, discNode);
			}

			addStreamToDisc(disc.getZeroStream(), disc, discNode);

			root.addChildNode(discNode);
			
			if (isAutumn) {
				this.discsListAutumn.addLast(disc);
			} else {
				this.discsListSpring.addLast(disc);
			}
		}

		return root;
	}

	private void addStreamToDisc(GroupStream stream, Discipline disc, Node discNode) {
		Node streamNode = new Node(new Integer(stream.getId()).toString(), stream.toString());
		streamNode.setIsSelected(0);
		streamNode.setAppointed(false);
		streamNode.setLoadNode(false);

		HashMap<String, Node> loadKindsNodes = new HashMap<>();

		Node groupLoadNode = null;
		if (stream.isMultiGroup()) {
			groupLoadNode = new Node("0", "");
			groupLoadNode.setLoadNode(true);
			groupLoadNode.setIsSelected(0);	
			StringBuilder name = new StringBuilder();

			for (int i = 0; i < stream.getItems().size(); i++) {
				GroupLoadItem item = stream.getItems().get(i);
				
				name.append(item.getName());
				if (groupLoadNode.isAppointed() && !item.isAppointed()) { 
					groupLoadNode.setAppointed(false);
				} else if (!groupLoadNode.isAppointed() && item.isAppointed()){
					groupLoadNode.setAppointed(true);
				}

				if (i < stream.getItems().size() - 1) {
					name.append(", ");
				}
			}

			groupLoadNode.setStreamId(stream.getId());
			groupLoadNode.setNodeName(name.toString());
			groupLoadNode.setDiscId(disc.id);
			groupLoadNode.setMultiNode(true);
			
			groupLoadNode.setStream(stream);
		}

		for (GroupLoadItem i : stream.getItems()) {
			if (!stream.isMultiGroup()) {
				groupLoadNode = new Node(new Integer(i.getId()).toString(), i.getName());
				groupLoadNode.setLoadNode(true);
				groupLoadNode.setAppointed(i.isAppointed());
				groupLoadNode.setIsSelected(0);
				
				groupLoadNode.setItem(i);
			}
			
			Node loadKindNode = loadKindsNodes.get(i.getKindLoad());

			if (loadKindNode == null) {
				loadKindNode = new Node("0", loadKindRenamingTable.get(i.getKindLoad()));
				loadKindsNodes.put(i.getKindLoad(), loadKindNode);

				streamNode.addChildNode(loadKindNode);
			}

			loadKindNode.addChildNode(groupLoadNode);
			
			if (stream.isMultiGroup()) {
				break;
			}
		}

		discNode.addChildNode(streamNode);
	}

	private LinkedList<GroupLoadItem> getGroupsForDiscipline(String discName, int version, boolean isAuthumn) throws SQLException {
		LinkedList<GroupLoadItem> result = new LinkedList<>();

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
			GroupLoadItem item = new GroupLoadItem(rs.getString(1), rs.getInt(2), rs.getString(3), rs.getInt(4), rs.getInt(5) != 0, rs.getInt(5));
			
			item.valueEP = rs.getInt(6);
			item.valueG = rs.getInt(7);
			item.valueCO = rs.getInt(8);
			item.valueCF = rs.getInt(9);
			
			item.nameDisc = rs.getString(10);
			
			result.add(item);
		}

		return result;		
	}

	private void getDiscNames(PreparedStatement ps, ArrayList<Discipline> discNames, int version) throws SQLException {
		ps.setInt(1, version);

		ps.execute();
		ResultSet res = ps.getResultSet();

		while (res.next()) {
			discNames.add(new Discipline(res.getString(1), res.getInt(2)));
		}
	}

	public String renameLoadKind(String old) {
		return loadKindRenamingTable.get(old);
	}
}
