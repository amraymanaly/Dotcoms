public final class DotComSettings {
	public boolean mayOverlap = false;
	public int numHits, minBorder, maxBorder;

	public DotComSettings(int numHits, int minBorder, int maxBorder, boolean mayOverlap) throws InstantiationException {
		if (minBorder >= maxBorder)
			throw new InstantiationException("minBorder cannot be greater than or equal maxBorder.");

		this.numHits = numHits;
		this.minBorder = minBorder;
		this.maxBorder = maxBorder;
		this.mayOverlap = mayOverlap;
	}
}
