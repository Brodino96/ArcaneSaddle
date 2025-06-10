package net.brodino.arcanesaddle.config;

import io.wispforest.owo.config.annotation.Config;
import net.brodino.arcanesaddle.ArcaneSaddle;

import java.util.List;

@Config(wrapperName = "Config", name = ArcaneSaddle.MOD_ID)
public class ConfigHelper {
    public int mountTimers = 40;
    public int itemCooldown = 20;
    public List<String> allowedDimension = List.of("minecraft:overworld");
}
