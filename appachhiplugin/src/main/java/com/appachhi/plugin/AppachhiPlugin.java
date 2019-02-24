package com.appachhi.plugin;

import com.android.build.gradle.AppExtension;
import com.android.build.gradle.LibraryExtension;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class AppachhiPlugin implements Plugin<Project> {

    @SuppressWarnings("NullableProblems")
    @Override
    public void apply(Project project) {
        Object ext = project.getExtensions().findByName("android");
        if (ext instanceof AppExtension) {
            AppExtension androidExt = (AppExtension) ext;
            project.getExtensions().create("Appachhi", AppachhiExtension.class);
            androidExt.registerTransform(new AppachhiTransform(project));
        } else if (ext instanceof LibraryExtension) {
            throw new RuntimeException("Appachhi cannot be applied to a library project.  It must only be used with an Android application project.");
        } else {
            throw new RuntimeException("Appachhi Plugin may only be applied to Android projects");
        }
    }
}
