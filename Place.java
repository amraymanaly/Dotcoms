import java.util.ArrayList;

public final class Place {
// class fields, setters and getters
	private ArrayList<DotCom> coms = new ArrayList<DotCom>();
	public final int num;

	public DotCom[] getResidents() {
		return coms.toArray(new DotCom[coms.size()]);
	}
// constructors
	public Place(int num, DotCom...coms) {
		this.num = num;

		for (DotCom com : coms)
			this.coms.add(com);
	}
// special methods
	public String toString() {
		return Integer.toString(num);
	}
// usage methods
	public boolean isOverlapped() {
		return (coms.size() > 1);
	}

	public DotCom[] hitResidents() {
		DotCom[] res = getResidents();

		for (DotCom com : res)
			com.hit(num);

		return res;
	}

	public void addResidents(DotCom...coms) {
		for (DotCom com : coms)
			this.coms.add(com);
	}
}
