import java.util.ArrayList;

public class CourseCatalog{
	private int id;
	private ArrayList<Course> catalog;
	
	// methods
	// add course to catalog array list
	public void addToCatalog(Course course) {
		this.catalog.add(course);
	}

	// remove course from catalog arraly list
	public void removeFromCatalog(Course course) {
		this.catalog.remove(course);
	}

	// get catalog array list
	public ArrayList<Course> getCatalog() {
		return this.catalog;
	}
}
