package com.example.randall.readit.data;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

@ContentProvider(authority = ScriptsProvider.AUTHORITY, database = ScriptsDatabase.class)
public final class ScriptsProvider {
    public static final String AUTHORITY = "com.example.randall.readit.data.ScriptsProvider";

    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);


    interface Path {
        String SCRIPTS = "scripts";
    }

    private static Uri buildUri(String... paths) {
        Uri.Builder builder = BASE_CONTENT_URI.buildUpon();
        for (String path : paths) {
            builder.appendPath(path);
        }
        return builder.build();
    }

    @TableEndpoint(table = ScriptsDatabase.SCRIPTS)
    public static class Scripts {
        @ContentUri(
                path = Path.SCRIPTS,
                type = "vnd.android.cursor.dir/planet",
                defaultSort = ScriptColumns.TITLE + " ASC")
        public static final Uri CONTENT_URI = buildUri(Path.SCRIPTS);

        @InexactContentUri(
                name = "SCRIPT_ID",
                path = Path.SCRIPTS + "/#",
                type = "vnd.android.cursor.item/planet",
                whereColumn = ScriptColumns._ID,
                pathSegment = 1)
        public static Uri withId(long id) {
            return buildUri(Path.SCRIPTS, String.valueOf(id));
        }
    }

}
