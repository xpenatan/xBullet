#include "btBulletDynamicsCommon.h"
#include "btBulletCollisionCommon.h"
#include "BulletCollision/CollisionDispatch/btGhostObject.h"
#include "BulletCollision/CollisionShapes/btConvexPolyhedron.h"
#include "BulletCollision/CollisionShapes/btHeightfieldTerrainShape.h"
#include "BulletCollision/CollisionShapes/btShapeHull.h"
#include "BulletDynamics/Character/btKinematicCharacterController.h"
#include "BulletSoftBody/btDefaultSoftBodySolver.h"
#include "BulletSoftBody/btSoftBody.h"
#include "BulletSoftBody/btSoftBodyHelpers.h"
#include "BulletSoftBody/btSoftBodyRigidBodyCollisionConfiguration.h"
#include "BulletSoftBody/btSoftRigidDynamicsWorld.h"
#include "BulletCollision/Gimpact/btGImpactCollisionAlgorithm.h"
#include "BulletCollision/Gimpact/btGImpactShape.h"
#include "BulletCollision/NarrowPhaseCollision/btRaycastCallback.h"

typedef btAlignedObjectArray<float>	btScalarArray;
typedef btAlignedObjectArray<btVector3> btVector3Array;
typedef btAlignedObjectArray<btFace> btFaceArray;
typedef btAlignedObjectArray<int> btIntArray;
typedef btAlignedObjectArray<btIndexedMesh> btIndexedMeshArray;
typedef btAlignedObjectArray<const btCollisionObject*> btConstCollisionObjectArray;
typedef btAlignedObjectArray<btScalar> btScalarArray;

#ifndef CUSTOM_CODE_H
#define CUSTOM_CODE_H

class CustomCode {
public:
    static int GetBTVersion() {
        return btGetVersion();
    }
};

#endif  // CUSTOM_CODE_H