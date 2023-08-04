package net.ioixd.spigotjsongen;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import com.google.gson.Gson;

public class App {

    public static void main(String[] args) throws IOException {
        WebScraper webScraper = new WebScraper();
        String[][] packages = new String[][] {
                { "java.util", "https://docs.oracle.com/javase/8/docs/api/", "java.util.Collection",
                        "java.util.Comparator", "java.util.Deque",
                        "java.util.Enumeration", "java.util.EventListener", "java.util.Formattable",
                        "java.util.Iterator", "java.util.List", "java.util.ListIterator", "java.util.Map",
                        "java.util.Map$Entry", "java.util.NavigableMap", "java.util.NavigableSet", "java.util.Observer",
                        "java.util.PrimitiveIterator", "java.util.PrimitiveIterator$OfDouble",
                        "java.util.PrimitiveIterator$OfInt", "java.util.PrimitiveIterator$OfLong", "java.util.Queue",
                        "java.util.RandomAccess", "java.util.Set", "java.util.SortedMap", "java.util.SortedSet",
                        "java.util.Spliterator", "java.util.Spliterator$OfDouble", "java.util.Spliterator$OfInt",
                        "java.util.Spliterator$OfLong", "java.util.Spliterator$OfPrimitive",
                        "java.util.AbstractCollection", "java.util.AbstractList", "java.util.AbstractMap",
                        "java.util.AbstractMap$SimpleEntry", "java.util.AbstractMap$SimpleImmutableEntry",
                        "java.util.AbstractQueue", "java.util.AbstractSequentialList", "java.util.AbstractSet",
                        "java.util.ArrayDeque", "java.util.ArrayList", "java.util.Arrays", "java.util.Base64",
                        "java.util.Base64$Decoder", "java.util.Base64$Encoder", "java.util.BitSet",
                        "java.util.Calendar", "java.util.Calendar$Builder", "java.util.Collections",
                        "java.util.Currency", "java.util.Date", "java.util.Dictionary",
                        "java.util.DoubleSummaryStatistics", "java.util.EnumMap", "java.util.EnumSet",
                        "java.util.EventListenerProxy", "java.util.EventObject", "java.util.FormattableFlags",
                        "java.util.Formatter", "java.util.GregorianCalendar", "java.util.HashMap", "java.util.HashSet",
                        "java.util.Hashtable", "java.util.IdentityHashMap", "java.util.IntSummaryStatistics",
                        "java.util.LinkedHashMap", "java.util.LinkedHashSet", "java.util.LinkedList",
                        "java.util.ListResourceBundle", "java.util.Locale", "java.util.Locale$Builder",
                        "java.util.Locale$LanguageRange", "java.util.LongSummaryStatistics", "java.util.Objects",
                        "java.util.Observable", "java.util.Optional", "java.util.OptionalDouble",
                        "java.util.OptionalInt", "java.util.OptionalLong", "java.util.PriorityQueue",
                        "java.util.Properties", "java.util.PropertyPermission", "java.util.PropertyResourceBundle",
                        "java.util.Random", "java.util.ResourceBundle", "java.util.ResourceBundle$Control",
                        "java.util.Scanner", "java.util.ServiceLoader", "java.util.SimpleTimeZone",
                        "java.util.Spliterators", "java.util.Spliterators$AbstractDoubleSpliterator",
                        "java.util.Spliterators$AbstractIntSpliterator",
                        "java.util.Spliterators$AbstractLongSpliterator", "java.util.Spliterators$AbstractSpliterator",
                        "java.util.SplittableRandom", "java.util.Stack", "java.util.StringJoiner",
                        "java.util.StringTokenizer", "java.util.Timer", "java.util.TimerTask", "java.util.TimeZone",
                        "java.util.TreeMap", "java.util.TreeSet", "java.util.UUID", "java.util.Vector",
                        "java.util.WeakHashMap", "java.util.regex.Pattern", "java.util.logging.Filter",
                        "java.util.logging.LoggingMXBean", "java.util.logging.ConsoleHandler",
                        "java.util.logging.ErrorManager", "java.util.logging.FileHandler",
                        "java.util.logging.Formatter", "java.util.logging.Handler", "java.util.logging.Level",
                        "java.util.logging.Logger", "java.util.logging.LoggingPermission",
                        "java.util.logging.LogManager", "java.util.logging.LogRecord",
                        "java.util.logging.MemoryHandler", "java.util.logging.SimpleFormatter",
                        "java.util.logging.SocketHandler", "java.util.logging.StreamHandler",
                        "java.util.logging.XMLFormatter", "java.util.concurrent.BlockingDeque",
                        "java.util.concurrent.BlockingQueue", "java.util.concurrent.Callable",
                        "java.util.concurrent.CompletableFuture",
                        "java.util.concurrent.CompletableFuture$AsynchronousCompletionTask",
                        "java.util.concurrent.CompletionService", "java.util.concurrent.CompletionStage",
                        "java.util.concurrent.ConcurrentMap", "java.util.concurrent.ConcurrentNavigableMap",
                        "java.util.concurrent.Delayed", "java.util.concurrent.Executor",
                        "java.util.concurrent.ExecutorService",
                        "java.util.concurrent.ForkJoinPool$ForkJoinWorkerThreadFactory",
                        "java.util.concurrent.ForkJoinPool$ManagedBlocker", "java.util.concurrent.Future",
                        "java.util.concurrent.RejectedExecutionHandler", "java.util.concurrent.RunnableFuture",
                        "java.util.concurrent.RunnableScheduledFuture", "java.util.concurrent.ScheduledExecutorService",
                        "java.util.concurrent.ScheduledFuture", "java.util.concurrent.ThreadFactory",
                        "java.util.concurrent.TransferQueue", "java.util.concurrent.atomic.AtomicBoolean",
                        "java.util.concurrent.TimeUnit",
                        "java.util.concurrent.atomic.AtomicInteger", "java.util.concurrent.atomic.AtomicIntegerArray",
                        "java.util.concurrent.atomic.AtomicIntegerFieldUpdater",
                        "java.util.concurrent.atomic.AtomicLong", "java.util.concurrent.atomic.AtomicLongArray",
                        "java.util.concurrent.atomic.AtomicLongFieldUpdater",
                        "java.util.concurrent.atomic.AtomicMarkableReference",
                        "java.util.concurrent.atomic.AtomicReference",
                        "java.util.concurrent.atomic.AtomicReferenceArray",
                        "java.util.concurrent.atomic.AtomicReferenceFieldUpdater",
                        "java.util.concurrent.atomic.AtomicStampedReference",
                        "java.util.concurrent.atomic.DoubleAccumulator", "java.util.concurrent.atomic.DoubleAdder",
                        "java.util.concurrent.atomic.LongAccumulator", "java.util.concurrent.atomic.LongAdder",
                        "java.util.concurrent.atomic.Striped64",
                        "java.util.function.BiFunction", "java.util.function.BinaryOperator",
                        "java.util.function.BiPredicate", "java.util.function.BooleanSupplier",
                        "java.util.function.Consumer", "java.util.function.DoubleBinaryOperator",
                        "java.util.function.DoubleConsumer", "java.util.function.DoubleFunction",
                        "java.util.function.DoublePredicate", "java.util.function.DoubleSupplier",
                        "java.util.function.DoubleToIntFunction", "java.util.function.DoubleToLongFunction",
                        "java.util.function.DoubleUnaryOperator", "java.util.function.Function",
                        "java.util.function.IntBinaryOperator", "java.util.function.IntConsumer",
                        "java.util.function.IntFunction", "java.util.function.IntPredicate",
                        "java.util.function.IntSupplier", "java.util.function.IntToDoubleFunction",
                        "java.util.function.IntToLongFunction", "java.util.function.IntUnaryOperator",
                        "java.util.function.LongBinaryOperator", "java.util.function.LongConsumer",
                        "java.util.function.LongFunction", "java.util.function.LongPredicate",
                        "java.util.function.LongSupplier", "java.util.function.LongToDoubleFunction",
                        "java.util.function.LongToIntFunction", "java.util.function.LongUnaryOperator",
                        "java.util.function.ObjDoubleConsumer", "java.util.function.ObjIntConsumer",
                        "java.util.function.ObjLongConsumer", "java.util.function.Predicate",
                        "java.util.function.Supplier", "java.util.function.ToDoubleBiFunction",
                        "java.util.function.ToDoubleFunction", "java.util.function.ToIntBiFunction",
                        "java.util.function.ToIntFunction", "java.util.function.ToLongBiFunction",
                        "java.util.function.ToLongFunction", "java.util.function.UnaryOperator",
                        "java.util.random.RandomGenerator",
                        "java.util.random.RandomGenerator$ArbitrarilyJumpableGenerator",
                        "java.util.random.RandomGenerator$JumpableGenerator",
                        "java.util.random.RandomGenerator$LeapableGenerator",
                        "java.util.random.RandomGenerator$SplittableGenerator",
                        "java.util.random.RandomGenerator$StreamableGenerator",
                        "java.util.random.RandomGeneratorFactory",
                        "java.util.stream.BaseStream", "java.util.stream.Collector",
                        "java.util.stream.Collector$Characteristics", "java.util.stream.Collectors",
                        "java.util.stream.DoubleStream", "java.util.stream.DoubleStream$Builder",
                        "java.util.stream.DoubleStream$DoubleMapMultiConsumer", "java.util.stream.IntStream",
                        "java.util.stream.IntStream$Builder", "java.util.stream.IntStream$IntMapMultiConsumer",
                        "java.util.stream.LongStream", "java.util.stream.LongStream$Builder",
                        "java.util.stream.LongStream$LongMapMultiConsumer", "java.util.stream.Stream",
                        "java.util.stream.Stream$Builder", "java.util.stream.StreamSupport",
                        "java.util.concurrent.locks.AbstractQueuedLongSynchronizer$ConditionObject"
                },
                { "org.bukkit",
                        "https://hub.spigotmc.org/javadocs/spigot/",
                        "org.bukkit.StructureType", "org.bukkit.World$Environment", "org.bukkit.BanList$Type",
                        "org.bukkit.plugin.ServicePriority", "org.bukkit.entity.EnderDragon$Phase",
                        "org.bukkit.conversations.ConversationAbandonedEvent", "org.bukkit.command.Command",
                        "org.bukkit.ChatColor", "org.bukkit.enchantments.EnchantmentTarget",
                        "org.bukkit.entity.Spellcaster$Spell", "org.bukkit.block.Lectern",
                        "org.bukkit.Warning$WarningState", "org.bukkit.material.Directional",
                        "org.bukkit.material.Openable", "org.bukkit.block.data.Directional",
                        "org.bukkit.entity.Damageable", "org.bukkit.block.data.Ageable",
                        "org.bukkit.block.data.type.Sapling", "org.bukkit.block.Furnace",
                        "org.bukkit.block.SculkSensor", "org.bukkit.block.Sign", "org.bukkit.structure.Structure",
                        "org.bukkit.block.data.type.StructureBlock$Mode", "org.bukkit.packs.DataPack$Compatibility",
                        "org.bukkit.packs.DataPack$Source", "org.bukkit.profile.PlayerTextures$SkinModel",
                        "org.bukkit.scoreboard.Team$Option", "org.bukkit.scoreboard.Team$OptionStatus",
                        "org.bukkit.block.data.type.TechnicalPiston$Type", "org.bukkit.block.data.type.Switch$Face",
                        "org.bukkit.block.data.type.Bamboo$Leaves", "org.bukkit.block.data.type.Jigsaw$Orientation",
                        "org.bukkit.block.data.type.Wall$Height", "org.bukkit.block.data.type.BigDripleaf$Tilt",
                        "org.bukkit.block.data.type.PointedDripstone$Thickness", "org.bukkit.block.data.type.Slab$Type",
                        "org.bukkit.block.data.FaceAttachable$AttachedFace", "org.bukkit.block.data.Rail$Shape",
                        "org.bukkit.block.data.Bisected$Half", "org.bukkit.block.data.type.Comparator$Mode",
                        "org.bukkit.block.data.type.Bell$Attachment", "org.bukkit.block.data.type.Stairs$Shape",
                        "org.bukkit.block.data.type.SculkSensor$Phase",
                        "org.bukkit.block.data.type.RedstoneWire$Connection", "org.bukkit.block.data.type.Bed$Part",
                        "org.bukkit.block.data.type.Door$Hinge", "org.bukkit.boss.DragonBattle$RespawnPhase",
                        "org.bukkit.entity.Ageable", "org.bukkit.entity.MushroomCow$Variant",
                        "org.bukkit.entity.Panda$Gene", "org.bukkit.entity.ItemDisplay$ItemDisplayTransform",
                        "org.bukkit.entity.AbstractArrow$PickupStatus", "org.bukkit.entity.Skeleton$SkeletonType",
                        "org.bukkit.entity.Warden$AngerLevel", "org.bukkit.entity.Rabbit$Type",
                        "org.bukkit.entity.TextDisplay$TextAlignment", "org.bukkit.entity.TropicalFish$Pattern",
                        "org.bukkit.entity.Wither$Head", "org.bukkit.entity.Llama$Color",
                        "org.bukkit.entity.Boat$Status", "org.bukkit.entity.Boat$Type",
                        "org.bukkit.entity.Display$Billboard", "org.bukkit.entity.Horse$Variant",
                        "org.bukkit.entity.Horse$Color", "org.bukkit.entity.Horse$Style",
                        "org.bukkit.entity.FishHook$HookState", "org.bukkit.entity.Parrot$Variant",
                        "org.bukkit.entity.Evoker$Spell", "org.bukkit.entity.Fox$Type", "org.bukkit.entity.Ocelot$Type",
                        "org.bukkit.entity.Cat$Type", "org.bukkit.entity.Axolotl$Variant",
                        "org.bukkit.entity.ArmorStand$LockType", "org.bukkit.entity.Sniffer$State",
                        "org.bukkit.Chunk$LoadLevel", "org.bukkit.Raid$RaidStatus", "org.bukkit.map.MapView$Scale",
                        "org.bukkit.block.Jukebox", "org.bukkit.inventory.meta.BookMeta$Generation",
                        "org.bukkit.block.data.type.Chest$Type", "org.bukkit.block.DecoratedPot$Side"
                },
                { "net.md_5",
                        "https://javadoc.io/doc/net.md-5/bungeecord-api/latest/",
                        "net.md_5.bungee.chat.TranslationRegistry$TranslationProvider" }

        };
        HashMap<String, Object> parsed_packages = new HashMap<>();

        for (String[] pkg : packages) {
            String doclink = new String();
            String[] lostImports = new String[] {};
            if (pkg.length >= 1) {
                doclink = pkg[1];
            }
            if (pkg.length >= 2) {
                lostImports = Arrays.copyOfRange(pkg, 2, pkg.length);
            }
            parsed_packages.put(pkg[0], packageMap(pkg[0], doclink, lostImports, webScraper));
        }

        File dest_file = new File("../spigot.json");
        dest_file.createNewFile();

        FileWriter dest = new FileWriter("../spigot.json");
        String json = new Gson().toJson(parsed_packages);
        dest.write(json, 0, json.length());
        dest.close();
    }

    public static HashMap<String, HashMap<String, Object>> packageMap(String packageName, String doclink,
            String[] lostImports, WebScraper webScraper) {
        Reflections reflections = new Reflections(packageName, new SubTypesScanner(false));
        HashMap<String, HashMap<String, Object>> all = new HashMap<>();

        // =======
        // CLASSES
        // =======
        ArrayList<ParsedClass> classes = new ArrayList<ParsedClass>();
        ArrayList<ClassParserFuture> futures = new ArrayList<>();
        int n = 0;
        var clsses = reflections.getSubTypesOf(Object.class);
        CountDownLatch latch = new CountDownLatch(clsses.size() + lostImports.length);
        for (Class<? extends Object> cls : clsses) {
            futures.add(new ClassParserFuture(n, cls, classes, doclink, webScraper, latch));
            n++;
        }
        for (String importStr : lostImports) {
            Class<?> what;
            try {
                what = Class.forName(importStr);
                futures.add(new ClassParserFuture(n, what, classes, doclink, webScraper, latch));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }

        for (var future : futures) {
            future.start();
        }
        latch.countDown();

        HashMap<String, ParsedClass> classes_part_2 = new HashMap<>();

        classes.forEach(c -> {
            classes_part_2.put(c.packageName + "." + c.name, c);
        });

        classes_part_2.keySet().forEach(c -> {
            ParsedClass cls = classes_part_2.get(c);
            if (all.get(cls.packageName) == null) {
                all.put(cls.packageName, new HashMap<>());
            }
            all.get(cls.packageName).put(c.replace(cls.packageName + ".", ""), cls);
        });

        // =======
        // ENUMS
        // =======
        ArrayList<Enum<?>> enum_objects = new ArrayList<>();
        for (Class<?> e : reflections.getSubTypesOf(Enum.class)) {
            try {
                if (e.getName().contains("$")) {
                    continue;
                }
                Method valueOf = e.getDeclaredMethod("valueOf", String.class);
                String value = e.getEnumConstants()[0].toString();
                if (value.toUpperCase() != value) {
                    value = value.replaceAll("([A-Z])", "_$1").toUpperCase();
                }
                enum_objects.add((Enum<?>) valueOf.invoke(null, value));
            } catch (InvocationTargetException ignored) {
            } catch (IllegalAccessException | IllegalArgumentException
                    | NoSuchMethodException | SecurityException e1) {
                String value = e.getEnumConstants()[0].toString();
                System.out.println(value);
                e1.printStackTrace();
            }
        }
        ;

        ArrayList<ParsedEnum> enums = new ArrayList<ParsedEnum>();
        for (Enum<?> e : enum_objects) {
            enums.add(new ParsedEnum(e, doclink, packageName, webScraper));
        }

        HashMap<String, ParsedEnum> enums_part_2 = new HashMap<>();

        enums.forEach(c -> {
            enums_part_2.put(c.name, c);
        });

        enums_part_2.keySet().forEach(c -> {
            ParsedEnum e = enums_part_2.get(c);
            if (all.get(e.packageName) == null) {
                all.put(e.packageName, new HashMap<>());
            }
            all.get(e.packageName).put(c, e);
        });

        return all;
    }

    public static ArrayList<String> getAnnotations(Class<?> cls) {
        ArrayList<String> ok = new ArrayList<>();
        for (Annotation annotation : cls.getAnnotations()) {
            Class<? extends Annotation> type = annotation.annotationType();

            for (Method method : type.getDeclaredMethods()) {
                Object value;
                try {
                    value = method.invoke(annotation, (Object[]) null);
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    e.printStackTrace();
                    return null;
                }
                ok.add(value.toString());
            }
        }
        return ok;
    }
}
