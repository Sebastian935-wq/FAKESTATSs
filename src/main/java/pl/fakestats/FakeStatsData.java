package pl.fakestats;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public class FakeStatsData {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("fakestats.json");
    public int kills = 1250;
    public int deaths = 83;
    public int streak = 17;
    public int top2 = 940;
    public int top3 = 710;

    public String getKd() {
        return deaths == 0 ? String.valueOf(kills) : String.format(java.util.Locale.ROOT, "%.2f", (double) kills / deaths);
    }

    public void load() {
        try {
            if (Files.exists(FILE)) {
                try (Reader reader = Files.newBufferedReader(FILE)) {
                    FakeStatsData loaded = GSON.fromJson(reader, FakeStatsData.class);
                    if (loaded != null) { kills = loaded.kills; deaths = loaded.deaths; streak = loaded.streak; top2 = loaded.top2; top3 = loaded.top3; }
                }
            }
        } catch (Exception ignored) { }
    }

    public void save() {
        try {
            Files.createDirectories(FILE.getParent());
            try (Writer writer = Files.newBufferedWriter(FILE)) { GSON.toJson(this, writer); }
        } catch (Exception ignored) { }
    }
}
