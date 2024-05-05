package team.exlab.ecohub.news.model;

public enum ENewsItemType {

	NEWS("NEWS"), GOOD_TO_KNOW("GOOD_TO_KNOW");

	ENewsItemType(String name) {
		this.name = name;
	}

	private String name;
}
