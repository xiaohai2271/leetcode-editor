package com.shuzijun.leetcode.plugin.setting;

import com.intellij.openapi.components.*;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.annotations.MapAnnotation;
import com.shuzijun.leetcode.plugin.model.LeetcodeEditor;
import com.shuzijun.leetcode.plugin.model.PluginConstant;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * @author shuzijun
 */
@State(name = "LeetcodeEditor" + PluginConstant.ACTION_SUFFIX, storages = {@Storage(value = PluginConstant.ACTION_PREFIX+"/editor.xml")})
public class ProjectConfig implements ProjectComponent, PersistentStateComponent<ProjectConfig.InnerState> {

    public ProjectConfig(Project project) {

    }

    @Nullable
    public static ProjectConfig getInstance(Project project) {
        return ServiceManager.getService(project, ProjectConfig.class);
    }

    private InnerState innerState = new InnerState();

    @Nullable
    @Override
    public ProjectConfig.InnerState getState() {
        return innerState;
    }

    @Override
    public void loadState(@NotNull ProjectConfig.InnerState innerState) {
        this.innerState = innerState;
    }


    public LeetcodeEditor getDefEditor(String path) {
        LeetcodeEditor leetcodeEditor = innerState.projectConfig.get(path);
        if (leetcodeEditor == null) {
            leetcodeEditor = new LeetcodeEditor();
            leetcodeEditor.setPath(path);
            innerState.projectConfig.put(path, leetcodeEditor);
        }
        return leetcodeEditor;
    }

    public LeetcodeEditor getEditor(String path) {
        return innerState.projectConfig.get(path);
    }

    public static class InnerState {
        @NotNull
        @MapAnnotation
        public Map<String, LeetcodeEditor> projectConfig;

        InnerState() {
            projectConfig = new HashMap<>();
        }
    }

    public String getComponentName() {
        return this.getClass().getName();
    }

    public void initComponent() {
    }

    public void disposeComponent() {
    }

    public void projectOpened() {
    }

    public void projectClosed() {
    }
}
