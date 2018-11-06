package hu.mgx.app.common;

/**
 * Handles languages and language items.
 *
 * @author MaG
 */
public interface LanguageHandlerInterface {

    /**
     * Clears all languages that was earlier added by <code>addLanguage</code>
     */
    public abstract void clearLanguages();

    /**
     * Sets whether <code>getLanguageString</code> tries to find a language item
     * on another language if it is not exists in the current language
     *
     * @param bSubstitute true when <code>getLanguageString</code> should try to
     * find a language item on another language if it is not exists in the
     * current language false when <code>getLanguageString</code> searches a
     * language item only in the current language
     */
    public abstract void setLanguageSubstitute(boolean bSubstitute);

    /**
     * Returns whether <code>getLanguageString</code> tries to find a language
     * item on another language if it is not exists in the current language
     *
     * @return the language substitution state
     */
    public abstract boolean isLanguageSubstitute();

    /**
     * Sets whether <code>getLanguageString</code> marks a language item when it
     * is not exists (with curly brackets)
     *
     * @param bMarkUndefined true when <code>getLanguageString</code> should
     * mark a language item with curly brackets when it is not exists
     */
    public abstract void setMarkUndefined(boolean bMarkUndefined);

    /**
     * Returns whether <code>getLanguageString</code> marks a language item with
     * curly brackets when it is not exists
     *
     * @return the mark undefined state
     */
    public abstract boolean isMarkUndefined();

    /**
     * Adds a language denoted by a name or code (eg. "hungarian" or "hu",
     * "english" or "en", etc.)
     *
     * @param sLanguage the name or code of a language
     * @return true if the added language is a new one, false otherwise
     */
    public abstract boolean addLanguage(String sLanguage);

    /**
     * Sets the current language
     *
     * @param sLanguage the language name or code
     * @return true if the language exists, false otherwise
     */
    public abstract boolean setLanguage(String sLanguage);

    /**
     * Returns the currently selected language name or code
     *
     * @return the currently selected language name or code (empty string if not
     * yet set)
     */
    public abstract String getLanguage();

    /**
     * Returns available language names or codes, except those ones that set by
     * <code>setEnabledLanguages</code>
     *
     * @return a <code>java.util.Vector</code> of String of available languages
     * names or codes.
     */
    public abstract java.util.Vector<String> getAvailableLanguages();

    /**
     * Sets the enabled languages. When it is set,
     * <code>getAvailableLanguages</code> returns all added languages, except
     * those ones that set by <code>setEnabledLanguages</code>
     * <p>
     * When it is set to an empty string, all available languages are enabled.
     *
     * @param sEnabledLanguages enabled language names or codes separated by
     * commas, pipes, or something non-letter character
     */
    public abstract void setEnabledLanguages(String sEnabledLanguages);

    /**
     * Sets a language item in the current language.
     *
     * @param iKey the key of the language item
     * @param sValue the language item
     */
    public abstract void setLanguageString(int iKey, String sValue);

    /**
     * Sets a language item.
     *
     * @param sLanguage the language name or code
     * @param iKey the key of the language item
     * @param sValue the language item
     */
    public abstract void setLanguageString(String sLanguage, int iKey, String sValue);

    /**
     * Returns the language item in the current language of the given key.
     *
     * @param iKey the key of the language item
     * @return the language item
     */
    public abstract String getLanguageString(int iKey);

    /**
     * Returns the language item in the current language of the given key or the
     * default value if it is not found.
     *
     * @param iKey the key of the language item
     * @param sDefaultValue the default value if the language item not found
     * @return the language item or the default value if it is not found
     */
    public abstract String getLanguageString(int iKey, String sDefaultValue);

    /**
     * Returns the language item of the given key or the default value if it is
     * not found.
     *
     * @param sLanguage the language name or code
     * @param iKey the key of the language item
     * @param sDefaultValue the default value if the language item not found
     * @return the language item or the default value if it is not found
     */
    public abstract String getLanguageString(String sLanguage, int iKey, String sDefaultValue);

    /**
     * Sets a language item in the current language.
     *
     * @param sKey the key of the language item
     * @param sValue the language item
     */
    public abstract void setLanguageString(String sKey, String sValue);

    /**
     * Sets a language item.
     *
     * @param sLanguage the language name or code
     * @param sKey the key of the language item
     * @param sValue the language item
     */
    public abstract void setLanguageString(String sLanguage, String sKey, String sValue);

    /**
     * Returns the language item in the current language of the given key.
     *
     * @param sKey the key of the language item
     * @return the language item
     */
    public abstract String getLanguageString(String sKey);

    /**
     * Returns the language item in the current language of the given key or the
     * default value if it is not found.
     *
     * @param sKey the key of the language item
     * @param sDefaultValue the default value if the language item not found
     * @return the language item or the default value if it is not found
     */
    public abstract String getLanguageString(String sKey, String sDefaultValue);

    /**
     * Returns the language item of the given key or the default value if it is
     * not found.
     *
     * @param sLanguage the language name or code
     * @param sKey the key of the language item
     * @param sDefaultValue the default value if the language item not found
     * @return the language item or the default value if it is not found
     */
    public abstract String getLanguageString(String sLanguage, String sKey, String sDefaultValue);

    /**
     * Sets a language item by xml string.
     *
     * @param sXML the xml string, eg.: &lt;key&gt;123&lt;/key&gt;&lt;item
     * lang=hu&gt;Mentés&lt;/item&gt;&lt;item lang=en&gt;Save&lt;/item&gt;
     *
     * @deprecated use <code>SwingApp.initXML</code> or
     * <code>SwingApp.addLanguageXML</code>
     */
    public abstract void setLanguageStringXML(String sXML);

}
