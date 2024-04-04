package com.link_intersystems.gradle.plugins.multimodule;

import org.gradle.api.Action;
import org.gradle.api.initialization.Settings;
import org.gradle.api.invocation.Gradle;
import org.mockito.ArgumentCaptor;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class GradleMocking {

    private Gradle gradle;

    public GradleMocking() {
        this.gradle = mock(Gradle.class);
    }

    public Gradle getGradle() {
        return gradle;
    }

    public Action<Settings> getSettingsEvaluatedAction() {
        ArgumentCaptor<Action<Settings>> captor = ArgumentCaptor.forClass(Action.class);
        verify(gradle).settingsEvaluated(captor.capture());

        return captor.getValue();
    }

    public void executeSettingsEvaluated(Settings settings) {
        getSettingsEvaluatedAction().execute(settings);
    }
}
