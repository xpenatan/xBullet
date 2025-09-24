import com.github.xpenatan.jparser.builder.BuildMultiTarget;
import com.github.xpenatan.jparser.builder.targets.EmscriptenTarget;
import com.github.xpenatan.jparser.builder.targets.WindowsMSVCTarget;
import com.github.xpenatan.jparser.builder.targets.WindowsTarget;
import com.github.xpenatan.jparser.builder.tool.BuildToolListener;
import com.github.xpenatan.jparser.builder.tool.BuildToolOptions;
import com.github.xpenatan.jparser.builder.tool.BuilderTool;
import com.github.xpenatan.jparser.idl.IDLReader;
import java.util.ArrayList;

public class Build {

    public static void main(String[] args) throws Exception {
        String libName = "bullet";
        String modulePrefix = "bullet";
        String basePackage = "bullet";
        String sourcePath =  "/build/bullet/src/";

        BuildToolOptions op = new BuildToolOptions(libName, basePackage, modulePrefix , sourcePath, args);
        BuilderTool.build(op, new BuildToolListener() {
            @Override
            public void onAddTarget(BuildToolOptions op, IDLReader idlReader, ArrayList<BuildMultiTarget> targets) {
                if(op.containsArg("teavm")) {
                    targets.add(getTeaVMTarget(op, idlReader));
                }
                if(op.containsArg("windows64")) {
                    targets.add(getWindowBuildTarget(op));
                }
//                if(op.containsArg("linux64")) {
//                    targets.add(getLinuxTarget(op));
//                }
//                if(op.containsArg("mac64")) {
//                    targets.add(getMacTarget(op, false));
//                }
//                if(op.containsArg("macArm")) {
//                    targets.add(getMacTarget(op, true));
//                }
//                if(op.containsArg("android")) {
//                    targets.add(getAndroidTarget(op));
//                }
//                if(op.containsArg("iOS")) {
//                    targets.add(getIOSTarget(op));
//                }
            }
        });



//        String libName = "bullet";
//        String basePackage = "bullet";
//
//        String bulletPath = new File("./../").getCanonicalPath().replace("\\", "/");
//        String bulletBasePath = bulletPath + "/base";
//        String bulletBuildPath = bulletPath + "/generator";
//        String bulletCPPPath = bulletPath + "/core";
//        String bulletTeavmPath = bulletPath + "/teavm";
//
//        String sourcePath = bulletBuildPath + "/build/bullet/bullet/src";
//
//        String idlPath = bulletBuildPath + "/src/main/cpp/bullet.idl";
//        IDLReader idlReader = IDLReader.readIDL(idlPath);
//
//        String cppBuildPath = bulletBuildPath + "/build/c++";
//        String libsDir = cppBuildPath + "/libs/";
//
//        String cppDestinationPath = cppBuildPath + "/src";
//        String libDestinationPath = cppDestinationPath + "/bullet";
//        String baseJavaDir = bulletBasePath + "/src/main/java";
//
//        FileHelper.copyDir(sourcePath, libDestinationPath);
//        FileHelper.copyDir("src/main/cpp/custom", libDestinationPath);
//
//        CppGenerator cppGenerator = new NativeCPPGenerator(libDestinationPath);
//        CppCodeParser cppParser = new CppCodeParser(cppGenerator, idlReader, basePackage, sourcePath);
//        cppParser.generateClass = true;
//        JParser.generate(cppParser, baseJavaDir, bulletCPPPath + "/src/main/java");
//
//        TeaVMCodeParser teavmParser = new TeaVMCodeParser(idlReader, libName, basePackage, sourcePath);
//        JParser.generate(teavmParser, baseJavaDir, bulletTeavmPath + "/src/main/java");
//
//        BuildConfig buildConfig = new BuildConfig(cppDestinationPath, cppBuildPath, libsDir, libName);
//
//        JBuilder.build(
//                buildConfig,
//                getWindowBuildTarget(cppBuildPath),
//                getEmscriptenBuildTarget(idlReader)
////                getAndroidBuildTarget()
//        );
    }

    private static BuildMultiTarget getWindowBuildTarget(BuildToolOptions op) {
        String libBuildCPPPath = op.getModuleBuildCPPPath();
        String sourceDir = op.getSourceDir();

        BuildMultiTarget multiTarget = new BuildMultiTarget();

        WindowsMSVCTarget windowsTarget = new WindowsMSVCTarget();
        windowsTarget.isStatic = true;
        windowsTarget.headerDirs.add("-I" + sourceDir);
        windowsTarget.cppInclude.add(sourceDir + "/BulletCollision/**.cpp");
        windowsTarget.cppInclude.add(sourceDir + "/BulletDynamics/**.cpp");
        windowsTarget.cppInclude.add(sourceDir + "/BulletSoftBody/**.cpp");
        windowsTarget.cppInclude.add(sourceDir + "/LinearMath/**.cpp");
        windowsTarget.cppFlags.add("-DBT_USE_INVERSE_DYNAMICS_WITH_BULLET2");
        multiTarget.add(windowsTarget);

        WindowsMSVCTarget linkTarget = new WindowsMSVCTarget();
        linkTarget.addJNIHeaders();
        linkTarget.headerDirs.add("-I" + sourceDir);
        linkTarget.headerDirs.add("-I" + op.getCustomSourceDir());
        linkTarget.cppInclude.add(libBuildCPPPath + "/src/jniglue/JNIGlue.cpp");
        linkTarget.linkerFlags.add(libBuildCPPPath + "/libs/windows/vc/bullet64_.lib");
        multiTarget.add(linkTarget);

        return multiTarget;
    }

    private static BuildMultiTarget getTeaVMTarget(BuildToolOptions op, IDLReader idlReader) {
        String libBuildCPPPath = op.getModuleBuildCPPPath();
        String sourceDir = op.getSourceDir();

        BuildMultiTarget multiTarget = new BuildMultiTarget();

        // Make a static library
        EmscriptenTarget libTarget = new EmscriptenTarget(idlReader);
        libTarget.isStatic = true;
        libTarget.compileGlueCode = false;
        libTarget.headerDirs.add("-I" + sourceDir);
        libTarget.cppInclude.add(sourceDir + "/BulletCollision/**.cpp");
        libTarget.cppInclude.add(sourceDir + "/BulletDynamics/**.cpp");
        libTarget.cppInclude.add(sourceDir + "/BulletSoftBody/**.cpp");
        libTarget.cppInclude.add(sourceDir + "/LinearMath/**.cpp");
        libTarget.cppFlags.add("-DBT_USE_INVERSE_DYNAMICS_WITH_BULLET2");
        multiTarget.add(libTarget);

        // Compile glue code and link
        EmscriptenTarget linkTarget = new EmscriptenTarget(idlReader);
        linkTarget.headerDirs.add("-I" + sourceDir);
        linkTarget.headerDirs.add("-include" + op.getCustomSourceDir() + "BulletCustom.h");
        linkTarget.linkerFlags.add(libBuildCPPPath + "/libs/emscripten/bullet_.a");
        linkTarget.cppFlags.add("-DBT_USE_INVERSE_DYNAMICS_WITH_BULLET2");
        multiTarget.add(linkTarget);

        return multiTarget;
    }
//
//    private static BuildMultiTarget getAndroidBuildTarget() {
//        BuildMultiTarget multiTarget = new BuildMultiTarget();
//
//        AndroidTarget androidTarget = new AndroidTarget();
//        androidTarget.headerDirs.add("src/bullet/");
//        androidTarget.cppInclude.add("**/src/bullet/BulletCollision/**.cpp");
//        androidTarget.cppInclude.add("**/src/bullet/BulletDynamics/**.cpp");
//        androidTarget.cppInclude.add("**/src/bullet/BulletSoftBody/**.cpp");
//        androidTarget.cppInclude.add("**/src/bullet/LinearMath/**.cpp");
//        androidTarget.cppFlags.add("-DBT_USE_INVERSE_DYNAMICS_WITH_BULLET2");
//
//        multiTarget.add(androidTarget);
//
//        return multiTarget;
//    }
}