const { test, before, after, beforeEach } = require('node:test');
const assert = require('node:assert');
const {
  initializeTestEnvironment,
  assertFails,
  assertSucceeds,
} = require('@firebase/rules-unit-testing');
const fs = require('fs');

const PROJECT_ID = 'demo-no-project';
const DATABASE_ID = 'ai-studio-android-shahpurg-d6f4fa4b-f179-47aa-adb6-f0255eb969e7';

let testEnv;

before(async () => {
  testEnv = await initializeTestEnvironment({
    projectId: PROJECT_ID,
    firestore: {
      rules: fs.readFileSync('firestore.rules', 'utf8'),
      host: '127.0.0.1',
      port: 8085,
    },
  });
});

after(async () => {
  if (testEnv) {
    await testEnv.cleanup();
  }
});

beforeEach(async () => {
  if (testEnv) {
    await testEnv.clearFirestore();
  }
});

test('Unauthenticated user cannot read or write orders', async () => {
  const unauthDb = testEnv.unauthenticatedContext().firestore();
  await assertFails(unauthDb.collection('orders').get());
  await assertFails(
    unauthDb.collection('orders').doc('order_123').set({
      orderId: 'order_123',
      customerId: 'user_1',
      customerName: 'Aman Kumar',
      customerPhone: '+919816012345',
      deliveryAddress: 'Main Chowk, Shahpur',
      totalAmount: 250,
      status: 'PLACED',
      createdAt: new Date(),
    })
  );
});

test('Authenticated customer can create and read their own order', async () => {
  const customerDb = testEnv.authenticatedContext('cust_123').firestore();
  const orderRef = customerDb.collection('orders').doc('order_123');

  await assertSucceeds(
    orderRef.set({
      orderId: 'order_123',
      customerId: 'cust_123',
      customerName: 'Aman Kumar',
      customerPhone: '+919816012345',
      deliveryAddress: 'Near Bus Stand, Shahpur',
      landmark: 'Opposite State Bank',
      itemsSummary: 'Milk 1L, Bread 1pkt',
      itemsCount: 2,
      totalAmount: 120.0,
      status: 'PLACED',
      paymentMethod: 'COD',
      notes: 'Call on arrival',
      createdAt: new Date(),
    })
  );

  const doc = await assertSucceeds(orderRef.get());
  assert.strictEqual(doc.data().customerName, 'Aman Kumar');
});

test('Admin with email can list all orders and update order status', async () => {
  // Setup order with customer context
  await testEnv.withSecurityRulesDisabled(async (context) => {
    await context.firestore().collection('orders').doc('order_456').set({
      orderId: 'order_456',
      customerId: 'cust_789',
      customerName: 'Sunita Devi',
      customerPhone: '+919816055555',
      deliveryAddress: 'Chambi Ground Road, Shahpur',
      totalAmount: 450.0,
      status: 'PLACED',
      createdAt: new Date(),
    });
  });

  const adminDb = testEnv.authenticatedContext('admin_uid', { email: 'admin@shahpurgo.com' }).firestore();
  
  // Admin can list all orders
  const snapshot = await assertSucceeds(adminDb.collection('orders').get());
  assert.strictEqual(snapshot.docs.length, 1);

  // Admin can update status to PACKING
  const orderRef = adminDb.collection('orders').doc('order_456');
  await assertSucceeds(
    orderRef.update({
      status: 'PACKING',
      updatedAt: new Date(),
    })
  );
});

test('Customer cannot update immutable fields or delete order', async () => {
  await testEnv.withSecurityRulesDisabled(async (context) => {
    await context.firestore().collection('orders').doc('order_111').set({
      orderId: 'order_111',
      customerId: 'cust_111',
      customerName: 'Rohit Sharma',
      customerPhone: '+919816011111',
      deliveryAddress: 'Dharamsala Road',
      totalAmount: 500.0,
      status: 'PLACED',
      createdAt: new Date(),
    });
  });

  const custDb = testEnv.authenticatedContext('cust_111').firestore();
  const orderRef = custDb.collection('orders').doc('order_111');

  // Customer cannot change totalAmount
  await assertFails(orderRef.update({ totalAmount: 10.0 }));

  // Deletion is prohibited
  await assertFails(orderRef.delete());
});
