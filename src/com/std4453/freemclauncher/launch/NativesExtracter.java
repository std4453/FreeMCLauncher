package com.std4453.freemclauncher.launch;

import java.io.File;
import java.util.List;
import java.util.Vector;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Expand;

import com.std4453.freemclauncher.files.DirectoryHelper;
import com.std4453.freemclauncher.libraries.Library;
import com.std4453.freemclauncher.libraries.LibraryIndexer;
import com.std4453.freemclauncher.profiles.Profile;

public class NativesExtracter {
	public NativesExtracter() {
	}

	public String extractNativesForProfile(Profile profile) {
		LibraryIndexer indexer = new LibraryIndexer();
		indexer.getLibrariesOfVersion(profile.getName());

		List<Library> natives = new Vector<Library>();

		for (Library library : indexer.getLibrariesOfVersion(profile.getName())) {
			if (library.passed())
				if (library.getNatives() != null
						&& !library.getNatives().isEmpty())
					natives.add(library);
		}

		String extractDir = "natives-" + profile.getName() + "-"
				+ System.currentTimeMillis();
		File extractDst = new File(DirectoryHelper.temp, extractDir);
		extractDst.mkdirs();

		for (Library library : natives) {
			String libraryName = library.getNatives();

			Project proj = new Project();
			Expand expand = new Expand();
			expand.setProject(proj);
			expand.setTaskName("unzip");
			expand.setTaskType("unzip");

			File nativesFile = new File(DirectoryHelper.libraries, libraryName);
			expand.setSrc(nativesFile);
			expand.setEncoding("UTF-8");

			expand.setDest(extractDst);
			expand.execute();
		}

		System.out.println(extractDst.getAbsolutePath());
		return extractDst.getAbsolutePath();
	}
}
