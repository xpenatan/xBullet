package bullet.linearmath;

import idl.IDLBase;

public class btIDebugDraw extends IDLBase {

    /*[-C++;-NATIVE]
        static jclass debugDrawClass = 0;
        static jmethodID drawLineID = 0;
        static jmethodID drawContactPointID = 0;
        static jmethodID getDebugModeID = 0;
        class CustomDebugDraw : public btIDebugDraw
        {
        private:
            JNIEnv* env;
            jobject obj;
        public:
            CustomDebugDraw( JNIEnv* env, jobject obj )
            {
                this->env = env;
                this->obj = obj;
            }
            virtual void drawLine(const btVector3& from, const btVector3& to, const btVector3& color)
            {
                env->CallVoidMethod(obj, drawLineID, (jlong)&from, (jlong)&to, (jlong)&color);
            }
            virtual void drawContactPoint(const btVector3& PointOnB, const btVector3& normalOnB, btScalar distance, int lifeTime, const btVector3& color)
            {
                env->CallVoidMethod(obj, drawContactPointID, (jlong)&PointOnB, (jlong)&normalOnB, distance, lifeTime, (jlong)&color);
            }
            virtual void reportErrorWarning(const char* warningString)
            {
            }
            virtual void draw3dText(const btVector3& location, const char* textString)
            {
            }
            virtual void setDebugMode(int debugMode)
            {
            }
            virtual int getDebugMode() const
            {
                return env->CallIntMethod(obj, getDebugModeID);
            }
        };
    */

    /*[-teaVM;-ADD]
        @org.teavm.jso.JSFunctor
        public interface DrawLine extends org.teavm.jso.JSObject {
            void drawLineJS(int vecFrom, int vecTo, int color);
        }
    */
    /*[-teaVM;-ADD]
        @org.teavm.jso.JSFunctor
        public interface DrawContactPoint extends org.teavm.jso.JSObject {
            void drawContactPointJS(int PointOnB, int normalOnB, float distance, int lifeTime, int color);
        }
    */
    /*[-teaVM;-ADD]
        @org.teavm.jso.JSFunctor
        public interface GetDebugMode extends org.teavm.jso.JSObject {
            int getDebugModeJS();
        }
    */

    /*[-teaVM;-REPLACE]
        public btIDebugDraw() {
            DrawLine drawLine = new DrawLine() {
                @Override
                public void drawLineJS (int vecFrom, int vecTo, int color) {
                    drawLine(vecFrom, vecTo, color);
                }
            };
            DrawContactPoint drawContactPoint = new DrawContactPoint() {
                @Override
                public void drawContactPointJS (int PointOnB, int normalOnB, float distance, int lifeTime, int color) {
                    drawContactPoint(PointOnB, normalOnB, distance, lifeTime, color);
                }
            };
            GetDebugMode getDebugMode = new GetDebugMode() {
                @Override
                public int getDebugModeJS () {
                    return getDebugMode();
                }
            };
            int pointer = createNative(drawLine, drawContactPoint, getDebugMode);
            initObject(pointer, true);
        }
    */
    public btIDebugDraw() {
        long addr = createNATIVE();
        initObject(addr, true);
    }

    /*[-C++;-NATIVE]
        if(debugDrawClass == 0) {
            debugDrawClass = (jclass)env->NewGlobalRef(env->GetObjectClass(object));
            drawLineID = env->GetMethodID(debugDrawClass, "drawLine", "(JJJ)V");
            drawContactPointID = env->GetMethodID(debugDrawClass, "drawContactPoint", "(JJFIJ)V");
            getDebugModeID = env->GetMethodID(debugDrawClass, "getDebugMode", "()I");
        }
        return (jlong)new CustomDebugDraw(env, env->NewGlobalRef(object));
    */
    /*[-teaVM;-REPLACE]
        @org.teavm.jso.JSBody(params = { "drawLine", "drawContactPoint", "getDebugMode"}, script = "var jsObj = new bullet.DebugDrawImpl(); jsObj.drawLine = drawLine; jsObj.drawContactPoint = drawContactPoint; jsObj.getDebugMode = getDebugMode; jsObj.reportErrorWarning = function() {}; jsObj.draw3dText = function() {};  jsObj.setDebugMode = function() {}; return bullet.getPointer(jsObj);")
        private static native int createNative(DrawLine drawLine, DrawContactPoint drawContactPoint, GetDebugMode getDebugMode);
    */
    private native long createNATIVE();

    btVector3 from = new btVector3((byte)1);
    btVector3 to = new btVector3((byte)1);
    btVector3 col = new btVector3((byte)1);

    protected void drawLine(long vecFrom, long vecTo, long color) {
        from.setPointer(vecFrom);
        to.setPointer(vecTo);
        col.setPointer(color);
        drawLine(from, to, col);
    }

    protected void drawContactPoint(long PointOnB, long normalOnB, float distance, int lifeTime, long color) {
        from.setPointer(PointOnB);
        to.setPointer(normalOnB);
        col.setPointer(color);
        drawContactPoint(from, to, distance, lifeTime, col);
    }

    public int getDebugMode() {
        return 0;
    }

    protected void drawLine(btVector3 vecFrom, btVector3 vecTo, btVector3 color) {
    }

    protected void drawContactPoint(btVector3 PointOnB, btVector3 normalOnB, float distance, int lifeTime, btVector3 color) {
    }
}