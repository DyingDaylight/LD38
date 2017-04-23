package com.mygdx.game.model;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.mygdx.game.data.Configuration;
import com.mygdx.game.data.ImageCache;

/**
 * Created by kettricken on 22.04.2017.
 */
public class Terrain {

    private final int BLOCK_SHIFT;
    private final int BLOCK_WIDTH = 128;
    private final int BLOCK_DEPTH;
    private final int WIDTH = 6;
    private final int HEIGHT = 6;
    private final int OFFSET_X;
    private final int OFFSET_Y;
    private final int GAP = 5;

    private Polygon collisionPolygon;

    private String[] tileNames = new String[WIDTH * HEIGHT];

    public Terrain() {

        BLOCK_SHIFT = ImageCache.getTexture("tile2").getRegionWidth() - BLOCK_WIDTH;
        BLOCK_DEPTH = ImageCache.getTexture("tile2").getRegionHeight() - BLOCK_WIDTH + GAP;

        collisionPolygon = new Polygon(new float[]{
                0, BLOCK_WIDTH,
                WIDTH * (BLOCK_WIDTH + GAP), BLOCK_WIDTH,
                WIDTH * (BLOCK_WIDTH + GAP) + HEIGHT * BLOCK_SHIFT, BLOCK_DEPTH * HEIGHT + BLOCK_WIDTH,
                HEIGHT * BLOCK_SHIFT, BLOCK_DEPTH * HEIGHT + BLOCK_WIDTH,
        });
        float terrain_width = collisionPolygon.getVertices()[4] - collisionPolygon.getVertices()[0];
        float terrain_height = collisionPolygon.getVertices()[5];
        OFFSET_X = (int) (Configuration.GAME_HEIGHT - terrain_width) / 2;
        OFFSET_Y = (int) (Configuration.GAME_WIDTH - terrain_height) / 2;

        for (int i = 0; i < collisionPolygon.getVertices().length; i+=2) {
            collisionPolygon.getVertices()[i] = collisionPolygon.getVertices()[i] + OFFSET_X;
            collisionPolygon.getVertices()[i+1] = collisionPolygon.getVertices()[i+1] + OFFSET_Y;
        }

        generateTiles();
    }

    private void generateTiles() {
        for (int i = 0; i < WIDTH * HEIGHT; i++) {
            int index = MathUtils.random(1, 2);
            tileNames[i] = String.format("tile%d", index);
        }
    }

    public void draw(Batch batch) {
        for (int i = 0; i < WIDTH; i++) {
            for (int j = HEIGHT-1; j >=0; j--) {
                int x = i * (BLOCK_WIDTH + GAP) + j * BLOCK_SHIFT + OFFSET_X;
                int y = j * BLOCK_DEPTH + OFFSET_Y;
                int k = j * WIDTH + i;
                batch.draw(ImageCache.getTexture(tileNames[k]), x, y);
            }
        }
    }

    public void debug(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(1, 0, 0, 1);
        shapeRenderer.polygon(collisionPolygon.getVertices());
        shapeRenderer.line(getLeftCape(), 0, getLeftCape(), 720);
        shapeRenderer.line(getRightCape(), 0, getRightCape(), 720);
        shapeRenderer.line(0, getTopCape(), 1280, getTopCape());
        shapeRenderer.line(0, getBottomCape(), 1280, getBottomCape());
    }

    public boolean contains(Player player) {
        return Intersector.isPointInPolygon(collisionPolygon.getVertices(),
                0, collisionPolygon.getVertices().length,
                player.getCollisionPoint().x, player.getCollisionPoint().y);
    }

    public int getLeftCape() {
        return (int) (collisionPolygon.getVertices()[6]);
    }

    public int getRightCape() {
        return (int) (collisionPolygon.getVertices()[4] - HEIGHT * BLOCK_SHIFT);
    }

    public int getBottomCape() {
        return (int) collisionPolygon.getVertices()[1];
    }

    public int getTopCape() {
        return (int) collisionPolygon.getVertices()[7];
    }


}
