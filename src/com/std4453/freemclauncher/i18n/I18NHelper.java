package com.std4453.freemclauncher.i18n;

import static com.std4453.freemclauncher.logging.Logger.*;

import java.awt.Component;
import java.awt.Container;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;

import com.std4453.freemclauncher.files.FileHelper;

public class I18NHelper {
	protected static Map<String, Map<String, String>> mappings;
	protected static List<Class<?>> setTextClasses = Arrays
			.asList(new Class<?>[] { JButton.class, JLabel.class,
					JCheckBox.class });

	protected static boolean overrideLocalLanguage;
	protected static String overriddenLanguage;

	static {
		mappings = new HashMap<String, Map<String, String>>();
		loadInternalLocalizations();
	}

	public static void addLocalizationEntries(String lang, String key,
			String value) {
		ensureLangMappingExists(lang);
		mappings.get(lang).put(key, value);
	}

	public static void addLocalizationEntries(String lang,
			Map<String, String> entries) {
		ensureLangMappingExists(lang);
		mappings.get(lang).putAll(entries);
	}

	public static String getLocalization(String lang, String name) {
		ensureLangMappingExists(lang);
		return mappings.get(lang).get(name);
	}

	public static String getDefaultLang() {
		return overrideLocalLanguage ? overriddenLanguage : String.format(
				"%s_%s", System.getProperty("user.language"),
				System.getProperty("user.country"));
	}

	public static String getLocalization(String name) {
		return getLocalization(getDefaultLang(), name);
	}

	public static String getNonNullLocalization(String name,
			String defaultString) {
		return getLocalization(name) == null ? defaultString == null ? name
				: defaultString : getLocalization(name);
	}

	public static String getNonNullLocalization(String name) {
		return getNonNullLocalization(name, name);
	}

	public static String getFormattedLocalization(String name, Object... params) {
		return String.format(getNonNullLocalization(name), params);
	}

	private static void ensureLangMappingExists(String lang) {
		if (mappings.get(lang) == null)
			mappings.put(lang, new HashMap<String, String>());
	}

	public static boolean loadLocalizations(String lang, File file) {
		if (!file.isFile())
			return false;

		String content = FileHelper.getFileContentAsString(file);
		return loadLocalizations(lang, content);
	}

	public static boolean loadLocalizations(String lang, String content) {
		ensureLangMappingExists(lang);

		Pattern pattern = Pattern.compile("^\\s*[\\.a-zA-Z0-9]+=.*?\\s*$");

		String[] lines = content.split("\n");
		for (String line : lines) {
			if (pattern.matcher(line).matches()) {
				line = line.trim();
				String key = line.substring(0, line.indexOf('='));
				String value = line.substring(line.indexOf('=') + 1);

				addLocalizationEntries(lang, key, value);
			}
		}

		return true;
	}

	public static boolean loadLocalizations(String lang, InputStream is) {
		if (is == null)
			return false;

		try {
			String content = FileHelper.toString(is);
			return loadLocalizations(lang, content);
		} catch (Throwable t) {
			log(ERROR, "Error while loading data of input stream");
			log(ERROR, t);

			return false;
		}
	}

	public static boolean loadDefaultLocalizations(String lang, File langFiles) {
		return loadLocalizations(lang, new File(langFiles, lang + ".lang"));
	}

	public static void loadDefaultLocalizations(File langFiles) {
		String lang = getDefaultLang();
		if (lang.equals("zh_CN") || lang.equals("en_US"))
			return;
		if (!loadDefaultLocalizations(lang, langFiles)) {
			log(WARNING, "Unable to load localization files for lang\"" + lang
					+ "\"");
			overrideLocalLanguage("zh_CN");
		}
	}

	public static void loadInternalLocalizations() {
		loadLocalizations("zh_CN",
				I18NHelper.class.getResourceAsStream("/assets/lang/zh_CN.lang"));
		loadLocalizations("en_US",
				I18NHelper.class.getResourceAsStream("/assets/lang/en_US.lang"));
	}

	public static void overrideLocalLanguage(String lang) {
		overrideLocalLanguage = true;
		overriddenLanguage = lang;
	}

	public static void localizeTexts(Container container) {
		for (int i = 0; i < container.getComponentCount(); ++i) {
			Component component = container.getComponent(i);
			if (component == null)
				continue;

			if (component instanceof Container)
				localizeTexts((Container) component);
			if (setTextClasses.contains(component.getClass())) {
				try {
					Class<?> clazz = component.getClass();
					Method methodGetText = clazz.getMethod("getText",
							(Class<?>[]) null);
					Method methodSetText = clazz.getMethod("setText",
							String.class);

					String text = (String) methodGetText.invoke(component,
							(Object[]) null);

					if (text.startsWith("i18n:")) {
						String name = text.substring(5);
						String localized = getNonNullLocalization(name);

						if (DEBUG_FLAG)
							log(DEBUG, "Localized string " + name + ", got "
									+ localized);

						methodSetText.invoke(component, localized);
					}
				} catch (NoSuchMethodException | SecurityException
						| IllegalAccessException | IllegalArgumentException
						| InvocationTargetException | ClassCastException e) {
					log(WARNING, "Can't set text!");
					log(WARNING, e);
				}
			}
		}
	}
}
