package org.hexnation.hexenchants.blocks;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.LinkedList;
import java.util.Queue;

public class BlockDetection {
    static LinkedList<Block> getNeighbors(Block current_block, Block BLOCK_TO_DETECT) {
        LinkedList<Block> neighbors = new LinkedList<>();


        for (int i = -1; i < 2; i++) {
            for (int j = 0; j < 10; j++) {
                BlockFace current_block_face = BlockFace.values()[j];
                Block block_to_check = null;

                if (!(current_block_face == BlockFace.UP)) {
                    block_to_check = current_block.getRelative(current_block_face.getModX(), current_block_face.getModY() + i, current_block_face.getModZ());
                } else {
                    block_to_check = current_block.getRelative(current_block_face.getModX(), i, current_block_face.getModZ());
                    j++;
                }

                // Use this to check for block type
                if (block_to_check.getType() == BLOCK_TO_DETECT.getType()) {
                    neighbors.add(block_to_check);
                }
            }
        }

        return neighbors;
    }

    public static LinkedList<Block> startDetectingBlocks(Block starting_block, Block BLOCK_TO_DETECT, int maximum_blocks) {
        LinkedList<Block> detected_blocks   = new LinkedList<>();
        Queue<Block> blocks_to_search       = new LinkedList<>(getNeighbors(starting_block, BLOCK_TO_DETECT));

        detected_blocks.add(starting_block);

        while (!blocks_to_search.isEmpty() && detected_blocks.size() <= maximum_blocks) {
            Block current_block = blocks_to_search.poll();

            if (detected_blocks.contains(current_block)) {
                continue;
            }

            LinkedList<Block> check_neighbors = getNeighbors(current_block, BLOCK_TO_DETECT);
            for (Block neighbor : check_neighbors) {
                if (!detected_blocks.contains(neighbor)) {
                    blocks_to_search.add(neighbor);
                }
            }

            detected_blocks.add(current_block);
        }

        return detected_blocks;
    }
}
