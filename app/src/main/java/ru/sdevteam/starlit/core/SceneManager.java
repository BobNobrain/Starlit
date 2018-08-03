package ru.sdevteam.starlit.core;

import java.util.Stack;

public final class SceneManager {
    private static Scene activeScene;
    private static Stack<Scene> previous = new Stack<>();
    private static SceneChangedListener listener = new SceneChangedListener() {
        @Override
        public void onSceneChanged(Scene newScene, Scene oldScene) {}
    };

    public static void setActiveScene(Scene s) {
        if (s == null) throw new NullPointerException("Scene to activate is null!");
        Scene old = activeScene;
        activeScene.stop();
        activeScene = s;
        s.launch();
        listener.onSceneChanged(s, old);
    }

    public static Scene getActiveScene() { return activeScene; }

    public static Scene pushScene(Scene s) {
        Scene result = activeScene;
        previous.push(activeScene);
        setActiveScene(s);
        return result;
    }
    public static Scene popScene() {
        Scene result = activeScene;
        Scene prev = previous.pop();
        setActiveScene(prev);
        return result;
    }

    public static void setSceneChangedListener(SceneChangedListener l) {
        listener = l;
    }

    public interface SceneChangedListener {
        void onSceneChanged(Scene newScene, Scene oldScene);
    }
}
