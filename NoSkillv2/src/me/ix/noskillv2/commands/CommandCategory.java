package me.ix.noskillv2.commands;

public enum CommandCategory {

	FUN(":gear:"),
	GAME(":musical_note:"),
	MISC(":game_die:"),
	MOD(":wink:"),
	MUSIC(":gun:");

    public final String label;

    private CommandCategory(String label) {
        this.label = label;
    }
    
    public String getLabel() {
		return label;
	}
}
