package com.example.randall.readit.data;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by Randall on 21/05/2016.
 */
@Database(version = ScriptsDatabase.VERSION)
public class ScriptsDatabase {

    public static final int VERSION = 1;

    @Table(ScriptColumns.class) public static final String SCRIPTS = "scripts";

}

