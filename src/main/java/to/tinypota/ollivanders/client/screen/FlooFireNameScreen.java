package to.tinypota.ollivanders.client.screen;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import to.tinypota.ollivanders.Ollivanders;
import to.tinypota.ollivanders.registry.common.OllivandersNetworking;

public class FlooFireNameScreen extends Screen {
    private TextFieldWidget textField;
    private ButtonWidget submitButton;
    private String defaultName;
	private BlockPos pos;
	private Direction direction;
	private boolean chosenRandomly;
	
	private ButtonWidget yesRandomButton;
	private ButtonWidget noRandomButton;
	
	private ButtonWidget northButton;
	private ButtonWidget southButton;
	private ButtonWidget eastButton;
	private ButtonWidget westButton;

    public FlooFireNameScreen() {
        super(Text.literal("Floo Fire Name Chooser"));
    }
	
	public FlooFireNameScreen(String defaultName, BlockPos pos, Direction direction, boolean chosenRandomly) {
		this();
		this.defaultName = defaultName;
		this.pos = pos;
		this.direction = direction;
		this.chosenRandomly = chosenRandomly;
	}

    @Override
    protected void init() {
        int textFieldWidth = 150;
        int textFieldHeight = 20;
        int textFieldX = (width - textFieldWidth) / 2;
        int textFieldY = (height - textFieldHeight) / 2 - 75;
				
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
		}).dimensions(buttonX, buttonY, buttonWidth, buttonHeight).build());
		
		String randomText = "Allow random navigation:";
		int randomLabelX = (width - textRenderer.getWidth(randomText)) / 2;
		int randomLabelY = buttonY + buttonHeight + 7;
		
		addDrawableChild(new TextWidget(randomLabelX, randomLabelY, textRenderer.getWidth(randomText), 9, Text.literal(randomText), textRenderer));
		
		int randomButtonWidth = 30;
		int randomButtonHeight = 20;
		int randomButtonX = (width - randomButtonWidth) / 2;
		int randomButtonY = buttonY + buttonHeight + 20;
		
		yesRandomButton = addDrawableChild(ButtonWidget.builder(Text.literal("Yes"), button -> {
			onChosenButtonPressed(button);
		}).dimensions(randomButtonX - 20, randomButtonY, randomButtonWidth, randomButtonHeight).build());
		
		noRandomButton = addDrawableChild(ButtonWidget.builder(Text.literal("No"), button -> {
			onChosenButtonPressed(button);
		}).dimensions(randomButtonX + 20, randomButtonY, randomButtonWidth, randomButtonHeight).build());
		setChosenButtonPressed(chosenRandomly);
		
		String facingText = "Facing direction:";
		int facingLabelX = (width - textRenderer.getWidth(facingText)) / 2;
		int facingLabelY = randomButtonY + randomButtonHeight + 7;
		
		addDrawableChild(new TextWidget(facingLabelX, facingLabelY, textRenderer.getWidth(facingText), 9, Text.literal(facingText), textRenderer));
		
		
		int arrowButtonSize = 20;
		int arrowButtonX = (width - arrowButtonSize) / 2;
		int arrowButtonY = randomButtonY + randomButtonHeight + 40;
	
		northButton = addDrawableChild(ButtonWidget.builder(Text.literal("N"), this::onArrowButtonPressed).dimensions(arrowButtonX, arrowButtonY - arrowButtonSize, arrowButtonSize, arrowButtonSize).build());
		southButton = addDrawableChild(ButtonWidget.builder(Text.literal("S"), this::onArrowButtonPressed).dimensions(arrowButtonX, arrowButtonY + arrowButtonSize, arrowButtonSize, arrowButtonSize).build());
		eastButton = addDrawableChild(ButtonWidget.builder(Text.literal("E"), this::onArrowButtonPressed).dimensions(arrowButtonX + arrowButtonSize, arrowButtonY, arrowButtonSize, arrowButtonSize).build());
		westButton = addDrawableChild(ButtonWidget.builder(Text.literal("W"), this::onArrowButtonPressed).dimensions(arrowButtonX - arrowButtonSize, arrowButtonY, arrowButtonSize, arrowButtonSize).build());
		setArrowButtonPressed(direction);
		
		setInitialFocus(textField);
    }
	
	private Direction getChosenDirection() {
		if (!northButton.active) {
			return Direction.NORTH;
		} else if (!southButton.active) {
			return Direction.SOUTH;
		} else if (!eastButton.active) {
			return Direction.EAST;
		} else if (!westButton.active) {
			return Direction.WEST;
		}
		return Direction.NORTH;
	}
	
	private boolean getChosenRandomly() {
		if (!yesRandomButton.active) {
			return true;
		} else {
			return false;
		}
	}
	
	private void setChosenButtonPressed(boolean chosenRandomly) {
		if (chosenRandomly) {
			yesRandomButton.active = false;
			noRandomButton.active = true;
		} else {
			yesRandomButton.active = true;
			noRandomButton.active = false;
		}
	}
	
	private void onChosenButtonPressed(ButtonWidget pressedButton) {
		yesRandomButton.active = yesRandomButton != pressedButton;
		noRandomButton.active = noRandomButton != pressedButton;
		Ollivanders.LOGGER.info(String.valueOf(getChosenRandomly()));
	}
 
	private void setArrowButtonPressed(Direction direction) {
		switch (direction) {
			case NORTH:
				onArrowButtonPressed(northButton);
				break;
			case SOUTH:
				onArrowButtonPressed(southButton);
				break;
			case EAST:
				onArrowButtonPressed(eastButton);
				break;
			case WEST:
				onArrowButtonPressed(westButton);
				break;
		}
	}
 
	private void onArrowButtonPressed(ButtonWidget pressedButton) {
		northButton.active = northButton != pressedButton;
		southButton.active = southButton != pressedButton;
		eastButton.active = eastButton != pressedButton;
		westButton.active = westButton != pressedButton;
	}
	
	@Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		renderInGameBackground(context);
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
		buf.writeInt(getChosenDirection().getId());
		buf.writeBoolean(getChosenRandomly());
		ClientPlayNetworking.send(OllivandersNetworking.SET_FLOO_FIRE_NAME, buf);
		super.close();
	}
}
