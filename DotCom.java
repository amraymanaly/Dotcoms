import java.util.ArrayList;
import java.util.Random;

public final class DotCom {
// class settings
	private static DotComSettings settings = null;
// global fields
	private static ArrayList<DotCom> allComs = new ArrayList<DotCom>();
// class fields, setters and getters
	private String domainName;
	private int hitsLeft;
	private ArrayList<Place> places = new ArrayList<Place>();

	public static DotCom[] getAllComs() {
		return allComs.toArray(new DotCom[allComs.size()]);
	}

	public static DotComSettings getSettings() {
		return settings;
	}

	public String getDomainName() {
		return domainName;
	}

	public Place[] getPlaces() {
		return places.toArray(new Place[places.size()]);
	}

	public int getHitsLeft() {
		return hitsLeft;
	}
// static methods
	public static void config(DotComSettings comSettings) {
		settings = comSettings;
	}

	public static DotCom[] hitAny(int place) {
		Place p = getPlaceGlobally(place);

		if (p != null)
			return p.hitResidents();

		return null;
	}

	private static Place getPlaceGlobally(int place) {
		for (DotCom com : allComs)
			for (Place p : com.places)
				if (p.num == place)
					return p;

		return null;
	}

	public static boolean isAllDead() {
		for (DotCom com : allComs)
			if (!com.isKilled())
				return false;

		return true;
	}

	public static void clearAllComs() {
		allComs.clear();
	}
// constructors
	public DotCom(String domainName) throws InstantiationException {
		if (settings == null)
			throw new InstantiationException("Please configure the DotCom class before calling any of its constructors.");

		if (allComs.size() == ((settings.maxBorder - settings.minBorder) / settings.numHits))
			throw new InstantiationException("Maximum size allowed according to border and place settings.");

		allComs.add(this);
		this.domainName = domainName;
		this.hitsLeft = settings.numHits;

		generatePlaces();
	}
// private methods
	private void generatePlaces() {
		Random rnd = new Random();
		Place place;

		for (int i = 0; i < settings.numHits;) {
			int tmp = rnd.nextInt(settings.maxBorder - settings.minBorder) + settings.minBorder;
			place = getPlaceGlobally(tmp);

			if (place != null && (!settings.mayOverlap || uses(place.num)))
				continue;

			if (place == null)
				place = new Place(tmp, this);
			else
				place.addResidents(this);

			places.add(place);
			++i;
		}
	}

	private Place getPlace(int place) {
		for (Place p : places)
			if (p.num == place)
				return p;

		return null;
	}
// usage methods
	public boolean hit(int place) {
		Place p = getPlace(place);
		if (p == null)
			return false;

		places.remove(p);
		--hitsLeft;
		return true;
	}

	public boolean uses(int place) {
		return (getPlace(place) != null);
	}

	public boolean isKilled() {
		return (hitsLeft == 0);
	}
// special methods
	public String toString() {
		return domainName;
	}
}
