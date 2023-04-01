package me.ix.noskillv2.commands;

public enum CommandCategory {

	MISC(":gear:"),
	MUSIC(":musical_note:"),
	FUN(":game_die:"),
	UTIL(":electric_plug:"),
	GAME(":gun:"),
	MOD(":tools:");

    public final String label;

    private CommandCategory(String label) {
        this.label = label;
    }
    
    public String getLabel() {
		return label;
	}
    
    public static CommandCategory getByName(String name) {
    	for(CommandCategory cat : CommandCategory.values()) {
    		if(cat.name().toLowerCase().equals(name.toLowerCase())) {
    			return cat;
    		}
    	}
    	return null;
    }
}
