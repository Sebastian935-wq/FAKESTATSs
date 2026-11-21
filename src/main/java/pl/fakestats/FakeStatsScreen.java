package pl.fakestats;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

public class FakeStatsScreen extends Screen {
    private TextFieldWidget kills, deaths, streak, top2, top3;
    private int left, top;

    public FakeStatsScreen() { super(Text.literal("FAKESTATS")); }

    @Override
    protected void init() {
        left = width / 2 - 105; top = height / 2 - 92;
        kills = field("Kills", FakeStatsClient.DATA.kills, top + 28);
        deaths = field("Deaths", FakeStatsClient.DATA.deaths, top + 52);
        streak = field("Killstreak", FakeStatsClient.DATA.streak, top + 76);
        top2 = field("TOP #2 kills", FakeStatsClient.DATA.top2, top + 100);
        top3 = field("TOP #3 kills", FakeStatsClient.DATA.top3, top + 124);
        addDrawableChild(ButtonWidget.builder(Text.literal("ZAPISZ"), b -> save()).dimensions(left, top + 153, 100, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("LOSOWE"), b -> {
            kills.setText(String.valueOf(100 + (int)(Math.random() * 5000)));
            deaths.setText(String.valueOf((int)(Math.random() * 300)));
            streak.setText(String.valueOf((int)(Math.random() * 100)));
        }).dimensions(left + 110, top + 153, 100, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("ZAMKNIJ"), b -> close()).dimensions(left + 55, top + 178, 100, 20).build());
    }

    private TextFieldWidget field(String label, int value, int y) {
        TextFieldWidget f = new TextFieldWidget(textRenderer, left + 92, y, 118, 20, Text.literal(label));
        f.setText(String.valueOf(value)); f.setMaxLength(9); addDrawableChild(f); return f;
    }

    private int number(TextFieldWidget field, int fallback) {
        try { return Math.max(0, Integer.parseInt(field.getText())); } catch (NumberFormatException e) { return fallback; }
    }

    private void save() {
        FakeStatsClient.DATA.kills = number(kills, FakeStatsClient.DATA.kills);
        FakeStatsClient.DATA.deaths = number(deaths, FakeStatsClient.DATA.deaths);
        FakeStatsClient.DATA.streak = number(streak, FakeStatsClient.DATA.streak);
        FakeStatsClient.DATA.top2 = number(top2, FakeStatsClient.DATA.top2);
        FakeStatsClient.DATA.top3 = number(top3, FakeStatsClient.DATA.top3);
        FakeStatsClient.DATA.save();
        close();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        context.fill(left - 10, top - 16, left + 220, top + 207, 0xD0202020);
        context.drawCenteredTextWithShadow(textRenderer, Text.literal("FAKESTATS").styled(s -> s.withBold(true)), width / 2, top - 7, 0xFFFFFF);
        String[] labels = {"Kills", "Deaths", "Killstreak", "TOP #2 kills", "TOP #3 kills"};
        for (int i = 0; i < labels.length; i++) context.drawTextWithShadow(textRenderer, labels[i], left, top + 34 + i * 24, 0xFFFFFF);
        super.render(context, mouseX, mouseY, delta);
    }
}
