package com.mygdx.game.model;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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
    private final int GAP = 10;

    private TextureRegion block;

    private Polygon collisionPolygon;

    public Terrain() {

        block = ImageCache.getTexture("tile");

        BLOCK_SHIFT = block.getRegionWidth() - BLOCK_WIDTH;
        BLOCK_DEPTH = block.getRegionHeight() - BLOCK_WIDTH;

        collisionPolygon = new Polygon(new float[]{
                0, BLOCK_WIDTH,
                BLOCK_WIDTH * WIDTH, BLOCK_WIDTH,
                WIDTH * BLOCK_WIDTH + HEIGHT * BLOCK_SHIFT, BLOCK_DEPTH * HEIGHT + BLOCK_WIDTH,
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

        //Intersector.overlapConvexPolygons(collisionPolygon, collisionPolygon);
    }

    public void draw(Batch batch) {
        for (int i = 0; i < WIDTH; i++) {
            for (int j = HEIGHT-1; j >=0; j--) {
                int x = i * BLOCK_WIDTH + j * BLOCK_SHIFT + OFFSET_X;
                int y = j * BLOCK_DEPTH + OFFSET_Y;
                batch.draw(block, x, y);
            }
        }
    }

    public void debug(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(1, 0, 0, 1);
        shapeRenderer.polygon(collisionPolygon.getVertices());
    }
}
