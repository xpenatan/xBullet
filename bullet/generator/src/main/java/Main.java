import com.github.xpenatan.jparser.builder.BuildConfig;
import com.github.xpenatan.jparser.builder.BuildMultiTarget;
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
import com.github.xpenatan.jparser.teavm.TeaVMCodeParser;
import java.io.File;

public class Main {

    public static void main(String[] args) throws Exception {
        generateAndBuild();
    }

    private static void generateAndBuild() throws Exception {
        String libName = "bullet";
        String basePackage = "bullet";

        String bulletPath = new File("./../").getCanonicalPath().replace("\\", "/");
        String bulletBasePath = bulletPath + "/base";
        String bulletBuildPath = bulletPath + "/generator";
        String bulletCPPPath = bulletPath + "/core";
        String bulletTeavmPath = bulletPath + "/teavm";

        String sourcePath = bulletBuildPath + "/build/bullet/bullet/src";

        String idlPath = bulletBuildPath + "/src/main/cpp/bullet.idl";
        IDLReader idlReader = IDLReader.readIDL(idlPath);

        String cppBuildPath = bulletBuildPath + "/build/c++";
        String libsDir = cppBuildPath + "/libs/";

        String cppDestinationPath = cppBuildPath + "/src";
        String libDestinationPath = cppDestinationPath + "/bullet";
        String baseJavaDir = bulletBasePath + "/src/main/java";

        FileHelper.copyDir(sourcePath, libDestinationPath);
        FileHelper.copyDir("src/main/cpp/custom", libDestinationPath);

        CppGenerator cppGenerator = new NativeCPPGenerator(libDestinationPath);
        CppCodeParser cppParser = new CppCodeParser(cppGenerator, idlReader, basePackage, sourcePath);
        cppParser.generateClass = true;
        JParser.generate(cppParser, baseJavaDir, bulletCPPPath + "/src/main/java");

        BuildConfig buildConfig = new BuildConfig(
                cppDestinationPath,
                cppBuildPath,
                libsDir,
                libName
        );

        TeaVMCodeParser teavmParser = new TeaVMCodeParser(idlReader, libName, basePackage, sourcePath);
        JParser.generate(teavmParser, baseJavaDir, bulletTeavmPath + "/src/main/java");

        JBuilder.build(
                buildConfig,
                getWindowBuildTarget(cppBuildPath),
                getEmscriptenBuildTarget(idlReader)
//                getAndroidBuildTarget()
        );
    }

    private static BuildMultiTarget getWindowBuildTarget(String cppBuildPath) {
        BuildMultiTarget multiTarget = new BuildMultiTarget();

        WindowsTarget windowsTarget = new WindowsTarget();
        windowsTarget.isStatic = true;
        windowsTarget.headerDirs.add("-Isrc/bullet/");
        windowsTarget.cppInclude.add("**/src/bullet/BulletCollision/**.cpp");
        windowsTarget.cppInclude.add("**/src/bullet/BulletDynamics/**.cpp");
        windowsTarget.cppInclude.add("**/src/bullet/BulletSoftBody/**.cpp");
        windowsTarget.cppInclude.add("**/src/bullet/LinearMath/**.cpp");
        windowsTarget.cppFlags.add("-DBT_USE_INVERSE_DYNAMICS_WITH_BULLET2");
        multiTarget.add(windowsTarget);

        WindowsTarget glueTarget = new WindowsTarget();
        glueTarget.addJNIHeaders();
        glueTarget.headerDirs.add("-Isrc/bullet/");
        glueTarget.cppInclude.add(cppBuildPath + "/src/jniglue/JNIGlue.cpp");
        glueTarget.linkerFlags.add(cppBuildPath + "/libs/windows/bullet64.a");
        multiTarget.add(glueTarget);

        return multiTarget;
    }

    private static BuildMultiTarget getEmscriptenBuildTarget(IDLReader idlReader) {
        BuildMultiTarget multiTarget = new BuildMultiTarget();

        EmscriptenTarget emscriptenTarget = new EmscriptenTarget(idlReader);
        emscriptenTarget.headerDirs.add("-Isrc/bullet");
        emscriptenTarget.headerDirs.add("-includesrc/bullet/BulletCustom.h");
        emscriptenTarget.cppInclude.add("**/src/bullet/BulletCollision/**.cpp");
        emscriptenTarget.cppInclude.add("**/src/bullet/BulletDynamics/**.cpp");
        emscriptenTarget.cppInclude.add("**/src/bullet/BulletSoftBody/**.cpp");
        emscriptenTarget.cppInclude.add("**/src/bullet/LinearMath/**.cpp");
        emscriptenTarget.cppFlags.add("-DBT_USE_INVERSE_DYNAMICS_WITH_BULLET2");

        multiTarget.add(emscriptenTarget);

        return multiTarget;
    }

    private static BuildMultiTarget getAndroidBuildTarget() {
        BuildMultiTarget multiTarget = new BuildMultiTarget();

        AndroidTarget androidTarget = new AndroidTarget();
        androidTarget.headerDirs.add("src/bullet/");
        androidTarget.cppInclude.add("**/src/bullet/BulletCollision/**.cpp");
        androidTarget.cppInclude.add("**/src/bullet/BulletDynamics/**.cpp");
        androidTarget.cppInclude.add("**/src/bullet/BulletSoftBody/**.cpp");
        androidTarget.cppInclude.add("**/src/bullet/LinearMath/**.cpp");
        androidTarget.cppFlags.add("-DBT_USE_INVERSE_DYNAMICS_WITH_BULLET2");

        multiTarget.add(androidTarget);

        return multiTarget;
    }
}