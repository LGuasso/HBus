package br.com.expressobits.hbus.provider;

import android.content.SearchRecentSuggestionsProvider;

/**
 * @author Rafael Correa
 * @since 23/02/17
 */

public class ItinerarySearchableProvider extends SearchRecentSuggestionsProvider {
    public static final String AUTHORITY = "br.com.expressobits.hbus.provider.ItinerarySearchableProvider";
    public static final int MODE = DATABASE_MODE_QUERIES;

    public ItinerarySearchableProvider(){
        setupSuggestions( AUTHORITY, MODE );
    }
}
