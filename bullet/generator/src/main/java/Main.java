import com.github.xpenatan.jparser.builder.BuildConfig;
import com.github.xpenatan.jparser.builder.BuildTarget;
import com.github.xpenatan.jparser.builder.JBuilder;
import com.github.xpenatan.jparser.builder.targets.AndroidTarget;
import com.github.xpenatan.jparser.builder.targets.EmscriptenTarget;
import com.github.xpenatan.jparser.builder.targets.WindowsTarget;
import com.github.xpenatan.jparser.core.JParser;
import com.github.xpenatan.jparser.core.util.FileHelper;
import com.github.xpenatan.jparser.cpp.CppCodeParser;
import com.github.xpenatan.jparser.cpp.CppGenerator;
import com.github.xpenatan.jparser.cpp.NativeCPPGenerator;
import com.github.xpenatan.jparser.idl.IDLReader;
import com.github.xpenatan.jparser.idl.parser.IDLDefaultCodeParser;
import com.github.xpenatan.jparser.teavm.TeaVMCodeParser;
import java.io.File;

public class Main {

    public static void main(String[] args) throws Exception {
        generate();
    }

    public static void generate() throws Exception {
        String basePackage = "bullet";
        String emscriptenCustomCodePath = new File("src/main/cpp/emscripten").getCanonicalPath();
        String idlPath = new File(emscriptenCustomCodePath + "/bullet.idl").getCanonicalPath();
        String emscriptenDir = new File("./build/bullet/").getCanonicalPath();
        String cppSourceDir = new File(emscriptenDir + "/bullet/src/").getCanonicalPath();
        String baseJavaDir = new File(".").getAbsolutePath() + "./base/src/main/java";
        IDLReader idlReader = IDLReader.readIDL(idlPath, cppSourceDir);

//        generateClassOnly(idlReader, basePackage, baseJavaDir);
        generateAndBuild(idlReader, basePackage, baseJavaDir, cppSourceDir, idlPath);
    }

    private static void generateClassOnly(
            IDLReader idlReader,
            String basePackage,
            String baseJavaDir
    ) throws Exception {
        IDLDefaultCodeParser idlParser = new IDLDefaultCodeParser(basePackage, "C++", idlReader);
        idlParser.generateClass = true;
        String genDir = "../core/src/main/java";
        JParser.generate(idlParser, baseJavaDir, genDir);
    }

    private static void generateAndBuild(
            IDLReader idlReader,
            String basePackage,
            String baseJavaDir,
            String cppSourceDir,
            String idlPath
    ) throws Exception {
        String libName = "bullet";
        String emscriptenCustomCodePath = new File("src/main/cpp/emscripten").getCanonicalPath();

        String libsDir = new File("./build/c++/libs/").getCanonicalPath();
        String genDir = "../core/src/main/java";
        String libBuildPath = new File("./build/c++/").getCanonicalPath();
        String cppDestinationPath = libBuildPath + "/src";
        String libDestinationPath = cppDestinationPath + "/bullet";

        FileHelper.copyDir(cppSourceDir, libDestinationPath);

        CppGenerator cppGenerator = new NativeCPPGenerator(libDestinationPath);
        CppCodeParser cppParser = new CppCodeParser(cppGenerator, idlReader, basePackage);
        cppParser.generateClass = true;
        JParser.generate(cppParser, baseJavaDir, genDir);

        BuildConfig buildConfig = new BuildConfig(
                cppDestinationPath,
                libBuildPath,
                libsDir,
                libName,
                emscriptenCustomCodePath
        );

        String teaVMgenDir = "../teavm/src/main/java/";
        TeaVMCodeParser teavmParser = new TeaVMCodeParser(idlReader, libName, basePackage);
        JParser.generate(teavmParser, baseJavaDir, teaVMgenDir);

        JBuilder.build(
                buildConfig,
                getWindowBuildTarget(),
                getEmscriptenBuildTarget(idlPath)
//                getAndroidBuildTarget()
        );
    }

    private static BuildTarget getWindowBuildTarget() {
        WindowsTarget windowsTarget = new WindowsTarget();
        windowsTarget.headerDirs.add("-Isrc/bullet/");
        windowsTarget.cppIncludes.add("**/src/bullet/BulletCollision/**.cpp");
        windowsTarget.cppIncludes.add("**/src/bullet/BulletDynamics/**.cpp");
        windowsTarget.cppIncludes.add("**/src/bullet/BulletSoftBody/**.cpp");
        windowsTarget.cppIncludes.add("**/src/bullet/LinearMath/**.cpp");
        windowsTarget.cppFlags.add("-DBT_USE_INVERSE_DYNAMICS_WITH_BULLET2");
        return windowsTarget;
    }

    private static BuildTarget getEmscriptenBuildTarget(String idlPath) {
        EmscriptenTarget teaVMTarget = new EmscriptenTarget(idlPath);
        teaVMTarget.headerDirs.add("-Isrc/bullet");
        teaVMTarget.headerDirs.add("-includesrc/jsglue/Bullet.h");
        teaVMTarget.headerDirs.add("-includesrc/jsglue/custom_glue.cpp");
        teaVMTarget.cppIncludes.add("**/src/bullet/BulletCollision/**.cpp");
        teaVMTarget.cppIncludes.add("**/src/bullet/BulletDynamics/**.cpp");
        teaVMTarget.cppIncludes.add("**/src/bullet/BulletSoftBody/**.cpp");
        teaVMTarget.cppIncludes.add("**/src/bullet/LinearMath/**.cpp");
        teaVMTarget.cppIncludes.add("**/src/jsglue/glue.cpp");
        teaVMTarget.cppFlags.add("-DBT_USE_INVERSE_DYNAMICS_WITH_BULLET2");
        return teaVMTarget;
    }

    private static BuildTarget getAndroidBuildTarget() {
        AndroidTarget androidTarget = new AndroidTarget();
        androidTarget.headerDirs.add("src/bullet/");
        androidTarget.cppIncludes.add("**/src/bullet/BulletCollision/**.cpp");
        androidTarget.cppIncludes.add("**/src/bullet/BulletDynamics/**.cpp");
        androidTarget.cppIncludes.add("**/src/bullet/BulletSoftBody/**.cpp");
        androidTarget.cppIncludes.add("**/src/bullet/LinearMath/**.cpp");
        androidTarget.cppFlags.add("-DBT_USE_INVERSE_DYNAMICS_WITH_BULLET2");
        return androidTarget;
    }
}