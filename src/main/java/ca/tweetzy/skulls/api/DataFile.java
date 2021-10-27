package ca.tweetzy.skulls.api;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

/**
 * The current file has been created by Kiran Hart
 * Date Created: September 19 2021
 * Time Created: 1:09 a.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public final class DataFile {

	private String path = "plugins" + File.separatorChar;
	private File ymlfile;
	private FileConfiguration fc;

	/**
	 * @param name of the file. can include folder paths (for instance
	 *             kits/users/Steve). Then the file Steve.yml would be in the folder
	 *             plugins/'plugin_name'/kits/users/Steve.yml
	 * @param jp   -> Your plugin main instance
	 */
	public DataFile(String name, JavaPlugin jp) {
		this.path = "plugins/" + jp.getName();
		this.ymlfile = new File(String.valueOf(path) + File.separatorChar + name + ".db");
		this.fc = (FileConfiguration) YamlConfiguration.loadConfiguration(this.ymlfile);
	}

	/**
	 * @param <T>
	 * @param path -> path in this config
	 * @return Object if found, nullable
	 * @throws ClassCastException
	 */
	@SuppressWarnings("unchecked")
	public <T> T getConfigField(String path) {
		FileConfiguration cfg = this.getConfig();
		Object o = cfg.get(path);
		if (o != null) {
			return (T) o;
		} else {
			cfg.set(path, "undefined");
			save();
		}
		return null;
	}

	/**
	 * @param <T>          -> Tries to cast the field value to the Class T
	 * @param whenNotFound -> What should get created in the config if field not exist
	 * @param path         -> path in this config
	 * @return Object T if found, null if cast failed
	 */
	@SuppressWarnings("unchecked")
	public <T> T getConfigField(String path, T whenNotFound) {
		FileConfiguration cfg = this.getConfig();
		Object o = cfg.get(path);
		if (o != null) {
			try {
				return (T) o;
			} catch (Exception e) {
				return null;
			}
		} else {
			cfg.set(path, whenNotFound);
			save();
		}
		return whenNotFound;
	}

	/**
	 * @param Path  of field
	 * @param Field value as Object
	 * @return whether the save was successful
	 */
	public boolean setField(String path, Object o) {
		getConfig().set(path, o);
		getConfig().contains("");
		return save();
	}

	public boolean contains(String path) {
		return getConfig().contains(path);
	}

	private FileConfiguration getConfig() {
		return this.fc;
	}

	/**
	 * @return true if the save was successful, otherwise false
	 */
	private boolean save() {
		File folder = new File(path);
		if (!folder.exists())
			folder.mkdirs();
		try {
			this.fc.save(this.ymlfile);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
}