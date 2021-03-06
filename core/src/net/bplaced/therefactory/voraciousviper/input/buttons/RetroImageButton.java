/*
 * Copyright (C) 2016  Christian DeTamble
 *
 * This file is part of Voracious Viper.
 *
 * Voracious Viper is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Voracious Viper is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Voracious Viper.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.bplaced.therefactory.voraciousviper.input.buttons;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import net.bplaced.therefactory.voraciousviper.misc.SettingsManager;

public class RetroImageButton extends AbstractRetroButton {

	private Drawable drawable;
	private AbstractRetroButton[] adjacentButtons;
	private String prefKey;
	private String prefValue;

	private RetroImageButton(ShapeRenderer shapeRenderer, int buttonSize, float thickness, Drawable drawable) {
		super(shapeRenderer, buttonSize, thickness);
		this.drawable = drawable;
    	addListener(new ClickListener() {
            @Override public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            	super.touchUp(event, x, y, pointer, button);
            	if (adjacentButtons != null) {
            		if (!isChecked()) {
            			adjacentButtons[0].setChecked(true);
            			if (((RetroImageButton)adjacentButtons[0]).getPrefKey() != null) {
	            			SettingsManager.getInstance().get().putString(
	            					((RetroImageButton)adjacentButtons[0]).getPrefKey(),
	            					((RetroImageButton)adjacentButtons[0]).getPrefValue()).flush();
            			}
            		} else {
		            	for (AbstractRetroButton adjacentButton : adjacentButtons) {
		                	adjacentButton.setChecked(false);
		                }
            		}
                }
            }
        });
	}
	
	private String getPrefValue() {
		return prefValue;
	}

	private String getPrefKey() {
		return prefKey;
	}

	public RetroImageButton(ShapeRenderer shapeRenderer, final int buttonSize, float thickness,
    		Drawable drawable, final String prefKey, final String prefValue) {
    	this(shapeRenderer, buttonSize, thickness, drawable);
    	this.prefKey = prefKey;
    	this.prefValue = prefValue;
    	addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            	super.clicked(event, x, y);
            	if (isChecked()) {
            		SettingsManager.getInstance().get().putString(prefKey, prefValue).flush();
            	}
            }
        });
    }
    
    RetroImageButton(ShapeRenderer shapeRenderer, int buttonSize, float thickness, Drawable drawable,
					 final String prefKey) {
		this(shapeRenderer, buttonSize, thickness, drawable);
		addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
            	boolean newValue = !SettingsManager.getInstance().get().getBoolean(prefKey, true);
            	SettingsManager.getInstance().get().putBoolean(prefKey, newValue).flush();
			}
        });
	}
    
	@Override
    public void draw(Batch batch, float parentAlpha) {
    	super.draw(batch, parentAlpha);
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * alpha);
        if (drawable != null) {
        	
        	drawable.draw(batch,
    		getX()+(getWidth()/2-drawable.getMinWidth()/2),
    		getY() + (getHeight()/2-drawable.getMinHeight()/2) + (isChecked() || isPressed() ? -1 : 0),
    		drawable.getMinWidth(),
    		drawable.getMinHeight());
        }
    }

	public void setAdjacentButtons(AbstractRetroButton[] adjacentButtons) {
		this.adjacentButtons = adjacentButtons;
	}
	
	void setDrawable(Drawable drawable) {
		this.drawable = drawable;
	}

}
