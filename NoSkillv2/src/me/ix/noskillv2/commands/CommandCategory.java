package me.ix.noskillv2.commands;

public enum CommandCategory {

	MISC(":gear:"),
	MUSIC(":musical_note:"),
	FUN(":game_die:"),
	NSFW(":wink:"),
	TARKOV(":gun:"),
	MOD(":tools:"),
	BITCOIN(":coin:");

    public final String label;

    private CommandCategory(String label) {
        this.label = label;
    }
    
    public String getLabel() {
		return label;
	}
}
