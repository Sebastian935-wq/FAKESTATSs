package pl.fakestats;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class FakeStatsClient implements ClientModInitializer {
    public static final FakeStatsData DATA = new FakeStatsData();
    private static KeyBinding openKey;

    @Override
    public void onInitializeClient() {
        DATA.load();
        openKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.fakestats.open", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_H,
                "category.fakestats"));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openKey.wasPressed()) {
                if (isTargetServer(client)) client.setScreen(new FakeStatsScreen());
            }
        });

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(literal("statystyki").executes(ctx -> {
                MinecraftClient c = MinecraftClient.getInstance();
                if (!isTargetServer(c)) { tell(c, "FAKESTATS działa tylko na anarchia.gg."); return 0; }
                c.setScreen(new FakeStatsScreen()); return 1;
            }));
            dispatcher.register(literal("topki").executes(ctx -> {
                MinecraftClient c = MinecraftClient.getInstance();
                if (!isTargetServer(c)) { tell(c, "FAKESTATS działa tylko na anarchia.gg."); return 0; }
                tell(c, "§6§lTOPKI (lokalna symulacja)");
                tell(c, "§e1. Ty §7- §f" + DATA.kills + " kills");
                tell(c, "§e2. Gracz_Example §7- §f" + DATA.top2 + " kills");
                tell(c, "§e3. Gracz_Example2 §7- §f" + DATA.top3 + " kills");
                return 1;
            }));
        });

        // Local fake sidebar. It never sends or changes scoreboard data on the server.
        HudRenderCallback.EVENT.register((drawContext, tickCounter) -> renderFakeSidebar(drawContext));
    }

    public static boolean isTargetServer(MinecraftClient client) {
        if (client == null || client.getCurrentServerEntry() == null) return false;
        String address = client.getCurrentServerEntry().address.toLowerCase();
        return address.equals("anarchia.gg") || address.endsWith(".anarchia.gg") || address.startsWith("anarchia.gg:");
    }

    private static void tell(MinecraftClient client, String message) {
        if (client.player != null) client.player.sendMessage(Text.literal(message), false);
    }

    private static void renderFakeSidebar(DrawContext ctx) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (!isTargetServer(client) || client.player == null || client.options.hudHidden) return;
        int x = client.getWindow().getScaledWidth() - 112;
        int y = 12;
        String[] lines = {"FAKESTATS", "Kills: " + DATA.kills, "Deaths: " + DATA.deaths,
                "K/D: " + DATA.getKd(), "Streak: " + DATA.streak};
        int width = 104;
        ctx.fill(x - 5, y - 5, x + width, y + 8 + lines.length * 10, 0x90000000);
        ctx.drawCenteredTextWithShadow(client.textRenderer, Text.literal(lines[0]).formatted(Formatting.GOLD, Formatting.BOLD), x + width / 2 - 2, y, 0xFFFFFF);
        for (int i = 1; i < lines.length; i++) {
            ctx.drawTextWithShadow(client.textRenderer, Text.literal(lines[i]).formatted(Formatting.WHITE), x, y + 10 + (i - 1) * 10, 0xFFFFFF);
        }
    }
}
