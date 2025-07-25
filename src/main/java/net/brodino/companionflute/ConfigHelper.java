package net.brodino.companionflute;

import io.wispforest.owo.config.annotation.Config;

import java.util.List;

@Config(wrapperName = "Config", name = CompanionFlute.MOD_ID)
public class ConfigHelper {
    public int mountTimers = 40;
    public int itemCooldown = 20;
    public List<String> allowedDimension = List.of("minecraft:overworld");
}
