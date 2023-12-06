package cn.xgpjun.xgpequipmentslot.apiWrapper;

import cn.xgpjun.xgpequipmentslot.XgpEquipmentSlot;
import cn.xgpjun.xgpequipmentslot.api.AttributeAPIWrapper;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class APIWrapperLoader {

    public static void loadAttributeAPI(){
        File folder = new File(XgpEquipmentSlot.getInstance().getDataFolder(), "AttributeAPI");
        File[] jarFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".jar"));
        if (jarFiles != null) {
            for (File jarFile : jarFiles) {
                try {
                    URL jarUrl =  jarFile.toURI().toURL();

                    try (URLClassLoader classLoader =  new URLClassLoader(new URL[]{jarUrl},AttributeAPIWrapper.class.getClassLoader());
                         JarFile jar = new JarFile(jarFile)) {
                        Enumeration<JarEntry> entries = jar.entries();

                        while (entries.hasMoreElements()) {
                            JarEntry entry = entries.nextElement();

                            if (entry.getName().endsWith(".class")) {
                                String className = entry.getName().replace("/", ".").replace(".class", "");
                                try {
                                    Class<?> targetClass = classLoader.loadClass(className);

                                    if (AttributeAPIWrapper.class.isAssignableFrom(targetClass)) {
                                        AttributeAPIWrapper attributeAPI = (AttributeAPIWrapper) targetClass.newInstance();
                                        attributeAPI.register();
                                        if(attributeAPI instanceof Listener){
                                            Bukkit.getPluginManager().registerEvents((Listener) attributeAPI,XgpEquipmentSlot.getInstance());
                                        }
                                    }

                                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}
