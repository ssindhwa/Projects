public class PubTest
{
	PubTest f;


    // treat each test* method below as an entry point, and
    // hence analyze it under `@' (i.e,, epsilon) context

    static void test1() {
	// example 1 starts here
	PubTest k1 = new PubTest();   // first object
	PubTest k2 = new PubTest();   // second object
	k1.f = k2;
	PubTest k3 = new PubTest();   // third object
	foo1(k1,k3);
	// first object's f points to second object, second object's f will point to third object here, and third object's f points to fourth object, and fourth object's f points to null
    }

    static void foo1(PubTest k1, PubTest k3)
    {
	// called from a single context. In this context, k1 points to first object, k3 points to third object, and first object's f points to second object
	PubTest k2 = k1.f;
	k2.f = k3;
	PubTest t = new PubTest(); // fourth object
	k3.f = t;
	t.f = null;
    }

    static void test2() {
	PubTest k1 = new PubTest(); // first object
	PubTest k2 = foo2(k1);
	PubTest k3 = new PubTest(); // second object
	PubTest k4 = foo2(k3);
	// k2 and k4 will point to both objects because the call depth was more than two
    }

    static PubTest foo2(PubTest k5)
    { // two contexts (both of length 1) reach here. In the first context, k5 points to first object. In the second context k5 points to second object.
	PubTest k7 = bar2(k5);
	// under both both contexts, k7 will point to both objects. (I think so. Abshishek and Devansh: Please confirm).
	return k7;
    }

    static PubTest bar2(PubTest k5)
    {
	// two contexts (both of length 2) reach here.  In the first context, k5 points to first object. In the second context k5 points to second object.
	PubTest k7 = bar3(k5);

	// under both both contexts, k7 will point to both objects.

	return k7;
    }

    static PubTest bar3(PubTest k6)
    {
	// due to length bound of two, only one context reaches here. Under this context, k6 can point to first object or second object.
	return k6;
    }

    static void test3() {
	PubTest k8 = new PubTest(); // first object
	PubTest k9 = baz2(k8);
	PubTest k10 = new PubTest(); // second object
	PubTest k11 = baz2(k10);
    }

    static PubTest baz2(PubTest k5)
    {
	// two contexts (both of length 1) reach here.  In the first context, k5 points to first object. In the second context k5 points to second object.
	PubTest k7 = baz3(k5);

	// k7 will point to first object in first context, and will point to second object in second context

	return k7;
    }

    static PubTest baz3(PubTest k6)
    {
	// two contexts (both of length 2) reach here.  In the first context, k6 points to first object. In the second context k6 points to second object.
	return k6;
    }
}

