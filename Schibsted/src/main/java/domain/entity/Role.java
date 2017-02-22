package domain.entity;

public class Role {

	private int id;
	private String name;
	private String page;
	
	public Role(int id, String name, String page){
		this.id = id;
		this.name = name;
		this.page = page;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	public String getPage() {
		return page;
	}
}
