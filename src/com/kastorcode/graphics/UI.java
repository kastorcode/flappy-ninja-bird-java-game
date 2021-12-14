package com.kastorcode.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.kastorcode.main.Game;


public class UI {
	public void render (Graphics g) {
		String score = Integer.toString((int)Game.score);
		g.setColor(Color.BLACK);
		g.setFont(new Font("serif", Font.BOLD, 24));
		g.drawString(score, 18, 33);

		g.setColor(new Color(227, 76, 15));
		g.drawString(score, 16, 32);
	}
}