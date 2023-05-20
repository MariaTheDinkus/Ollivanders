package to.tinypota.ollivanders.client.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class FlooFireNameScreen extends Screen {
    private TextFieldWidget textField;
    private ButtonWidget submitButton;
	private BlockPos pos;

    public FlooFireNameScreen() {
        super(Text.literal("My GUI"));
    }
	
	public FlooFireNameScreen(BlockPos pos) {
		this();
		this.pos = pos;
	}

    @Override
    protected void init() {
        int textFieldWidth = 150;
        int textFieldHeight = 20;
        int textFieldX = (this.width - textFieldWidth) / 2;
        int textFieldY = (this.height - textFieldHeight) / 2;

        this.textField = new TextFieldWidget(this.textRenderer, textFieldX, textFieldY, textFieldWidth, textFieldHeight, Text.literal(""));
        this.addDrawable(this.textField);

        int buttonWidth = 100;
        int buttonHeight = 20;
        int buttonX = (this.width - buttonWidth) / 2;
        int buttonY = textFieldY + textFieldHeight + 10;

        this.submitButton = ButtonWidget.builder(Text.literal("Submit"), button -> {
			
		}).position(buttonX, buttonY).size(buttonWidth, buttonHeight).build();
    }
	
	@Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        this.textField.render(context, mouseX, mouseY, delta);
        this.submitButton.render(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
    }
}
