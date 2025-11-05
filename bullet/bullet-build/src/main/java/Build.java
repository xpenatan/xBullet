import com.github.xpenatan.jParser.builder.BuildMultiTarget;
import com.github.xpenatan.jParser.builder.targets.AndroidTarget;
import com.github.xpenatan.jParser.builder.targets.EmscriptenTarget;
import com.github.xpenatan.jParser.builder.targets.WindowsMSVCTarget;
import com.github.xpenatan.jParser.builder.tool.BuildToolListener;
import com.github.xpenatan.jParser.builder.tool.BuildToolOptions;
import com.github.xpenatan.jParser.builder.tool.BuilderTool;
import com.github.xpenatan.jParser.idl.IDLReader;
import java.util.ArrayList;

public class Build {

    public static void main(String[] args) throws Exception {
        String libName = "bullet";
        String modulePrefix = "bullet";
        String basePackage = "bullet";
        String sourcePath =  "/build/bullet/src/";

        BuildToolOptions.BuildToolParams data = new BuildToolOptions.BuildToolParams();
        data.libName = libName;
        data.idlName = libName;
        data.webModuleName = libName;
        data.packageName = basePackage;
        data.cppSourcePath = sourcePath;
        data.modulePrefix = modulePrefix;

        BuildToolOptions op = new BuildToolOptions(data, args);
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
                if(op.containsArg("android")) {
                    targets.add(getAndroidBuildTarget(op));
                }
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

    private static BuildMultiTarget getAndroidBuildTarget(BuildToolOptions op) {
        BuildMultiTarget multiTarget = new BuildMultiTarget();
        String sourceDir = op.getSourceDir();
        String libBuildCPPPath = op.getModuleBuildCPPPath();

        AndroidTarget.ApiLevel apiLevel = AndroidTarget.ApiLevel.Android_10_29;
        ArrayList<AndroidTarget.Target> targets = new ArrayList<>();

        targets.add(AndroidTarget.Target.x86);
        targets.add(AndroidTarget.Target.x86_64);
        targets.add(AndroidTarget.Target.armeabi_v7a);
        targets.add(AndroidTarget.Target.arm64_v8a);

        for(int i = 0; i < targets.size(); i++) {
            AndroidTarget.Target target = targets.get(i);

            // Make a static library
            AndroidTarget androidTarget = new AndroidTarget(target, apiLevel);
            androidTarget.isStatic = true;
            androidTarget.headerDirs.add("-I" + sourceDir);
            androidTarget.headerDirs.add("-I" + sourceDir);
            androidTarget.cppInclude.add(sourceDir + "/BulletCollision/**.cpp");
            androidTarget.cppInclude.add(sourceDir + "/BulletDynamics/**.cpp");
            androidTarget.cppInclude.add(sourceDir + "/BulletSoftBody/**.cpp");
            androidTarget.cppInclude.add(sourceDir + "/LinearMath/**.cpp");
            androidTarget.cppFlags.add("-DBT_USE_INVERSE_DYNAMICS_WITH_BULLET2");
            multiTarget.add(androidTarget);

            // Compile glue code and link
            AndroidTarget linkTarget = new AndroidTarget(target, apiLevel);
            linkTarget.addJNIHeaders();
            linkTarget.headerDirs.add("-I" + sourceDir);
            linkTarget.headerDirs.add("-I" + op.getCustomSourceDir());
            linkTarget.cppInclude.add(libBuildCPPPath + "/src/jniglue/JNIGlue.cpp");
            linkTarget.linkerFlags.add(libBuildCPPPath + "/libs/android/" + target.getFolder() +"/lib" + op.libName + ".a");
            linkTarget.cppFlags.add("-DBT_USE_INVERSE_DYNAMICS_WITH_BULLET2");
            linkTarget.linkerFlags.add("-Wl,-z,max-page-size=16384");
            multiTarget.add(linkTarget);
        }

        return multiTarget;
    }
}