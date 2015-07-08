package com.std4453.freemclauncher.launch;

import java.io.File;
import java.util.List;

import com.std4453.freemclauncher.files.DirectoryHelper;
import com.std4453.freemclauncher.libraries.Library;
import com.std4453.freemclauncher.libraries.LibraryIndexer;
import com.std4453.freemclauncher.profiles.Profile;

public class LibrariesCommandLineBuilder {
	public static String getJavaClassPath(Profile profile) {
		LibraryIndexer indexer = new LibraryIndexer();
		List<Library> libraries = indexer.getLibrariesOfVersion(profile
				.getName());

		StringBuilder sb = new StringBuilder();
		for (Library library : libraries) {
			if (library.getNatives() == null)
				sb.append(new File(DirectoryHelper.libraries, library.getPath())
						.getAbsolutePath() + File.pathSeparatorChar);
		}

		File file = new File(DirectoryHelper.versions, String.format(
				"%s/%s.jar", profile.getName(), profile.getName()));
		sb.append(file.getAbsolutePath());

		return sb.toString();
	}
}
