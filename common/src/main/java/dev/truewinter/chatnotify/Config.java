package dev.truewinter.chatnotify;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;

import java.io.File;
import java.io.IOException;

/**
 * @hidden
 */
public class Config {
    private ChatNotify plugin = ChatNotify.getInstance();

    private String requireApproval = "none";
    private int requireApprovalLength = 0;
    private boolean initSetupDone = false;
    private YamlDocument config;

    public void loadConfig(File configFile) throws IOException {
        config = YamlDocument.create(
                configFile,
                plugin.getClass().getClassLoader().getResourceAsStream("config.yml"),
                GeneralSettings.DEFAULT,
                LoaderSettings.builder().setAutoUpdate(true).build(),
                DumperSettings.DEFAULT,
                UpdaterSettings.builder().setVersioning(new BasicVersioning("config-version")).build()
        );

        requireApproval = config.getString("require-approval");
        requireApprovalLength = config.getInt("require-approval-length");
        initSetupDone = config.getBoolean("init-setup-done");
    }

    public void reloadConfig() throws IOException {
        config.reload();
        loadConfig(config.getFile());
    }

    public String getRequireApproval() {
        return requireApproval;
    }

    public int getRequireApprovalLength() {
        return requireApprovalLength;
    }

    public boolean isInitSetupDone() {
        return initSetupDone;
    }

    protected void setInitSetupDone(boolean done) throws IOException {
        initSetupDone = done;
        config.set("init-setup-done", done);
        config.save();
    }
}
