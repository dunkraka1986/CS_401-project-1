import java.util.ArrayList;

public class CourseCatalog{
	private int id;
	private ArrayList<Course> catalog;
	
	// methods
	public void addToCatalog(Course course) {
		this.catalog.add(course);
	}
	
	public void removeFromCatalog(Course course) {
		this.catalog.remove(course);
	}
	
	public ArrayList<Course> getCatalog() {
		return this.catalog;
	}
}