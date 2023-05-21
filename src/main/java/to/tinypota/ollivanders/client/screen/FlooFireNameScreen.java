package to.tinypota.ollivanders.client.screen;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import to.tinypota.ollivanders.Ollivanders;
import to.tinypota.ollivanders.registry.common.OllivandersNetworking;

public class FlooFireNameScreen extends Screen {
    private TextFieldWidget textField;
    private ButtonWidget submitButton;
    private String defaultName;
	private BlockPos pos;

    public FlooFireNameScreen() {
        super(Text.literal("Floo Fire Name Chooser"));
    }
	
	public FlooFireNameScreen(String defaultName, BlockPos pos) {
		this();
		this.defaultName = defaultName;
		this.pos = pos;
	}
 
	public FlooFireNameScreen(BlockPos pos) {
		this();
		this.defaultName = "";
		this.pos = pos;
	}

    @Override
    protected void init() {
        int textFieldWidth = 150;
        int textFieldHeight = 20;
        int textFieldX = (width - textFieldWidth) / 2;
        int textFieldY = (height - textFieldHeight) / 2;

        textField = addDrawableChild(new TextFieldWidget(textRenderer, textFieldX, textFieldY, textFieldWidth, textFieldHeight, Text.literal("")));
        textField.setMaxLength(25);
        textField.setText(defaultName);

        int buttonWidth = 100;
        int buttonHeight = 20;
        int buttonX = (width - buttonWidth) / 2;
        int buttonY = textFieldY + textFieldHeight + 10;
	
		submitButton = addDrawableChild(ButtonWidget.builder(Text.literal("Submit"), button -> {
			if (!textField.getText().isEmpty()) {
				close();
			}
		}).position(buttonX, buttonY).size(buttonWidth, buttonHeight).build());
		
		setInitialFocus(textField);
    }
	
	@Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		renderBackground(context);
		textField.render(context, mouseX, mouseY, delta);
		submitButton.render(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
    }
	
	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (super.keyPressed(keyCode, scanCode, modifiers)) {
			return true;
		}
		
		return textField.keyPressed(keyCode, scanCode, modifiers);
	}
	
	@Override
	public boolean charTyped(char chr, int modifiers) {
		return textField.charTyped(chr, modifiers);
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (super.mouseClicked(mouseX, mouseY, button)) {
			return true;
		}
		
		return textField.mouseClicked(mouseX, mouseY, button);
	}
	
	@Override
	public boolean shouldCloseOnEsc() {
		return !textField.getText().isEmpty() && super.shouldCloseOnEsc();
	}
	
	@Override
	public void close() {
		var buf = PacketByteBufs.create();
		buf.writeString(textField.getText());
		buf.writeBlockPos(pos);
		ClientPlayNetworking.send(OllivandersNetworking.SET_FLOO_FIRE_NAME, buf);
		super.close();
	}
}
