find = function(tree, value) {
    var node = tree.root;

    while(node !== undefined) {
        if(node.value == value) {
            return node;
        } else if(value < node.value) {
            node = node.left;
        } else {
            node = node.right;
        }
    }

    return null;
}

leastCommonAncestor = function(tree, value1, value2) {
    var min = Math.min(value1, value2);
    var max = Math.max(value1, value2);

    var node = tree.root;

    // console.log("> " + min + ", " + max);
    while(true) {
        // console.log(node.value);

        if(min <= node.value && max >= node.value) {
            return node;
        } else if(node.value < min) {
            node = node.right;
        } else {
            node = node.left;
        }
    }
}

describe("tree/BinarySearchTree", function() {
    var tree;

    beforeEach(function() {
        tree = {
            root : {
                value: 16,
                left : {
                    value: 8,
                    left : { value: 4 },
                    right : { value: 12 }
                },
                right : {
                    value: 24,
                    left : { value: 20 },
                    right : { value: 28 }
                }
            }
        }
    });

    describe("Retrieving Elements", function() {
        it("should retrieve a node, given its key", function() {
            expect(find(tree, 16)).toBe(tree.root);
            expect(find(tree, 8)).toBe(tree.root.left);
            expect(find(tree, 24)).toBe(tree.root.right);
            expect(find(tree, 28)).toEqual({value: 28});
        });

        it("should return null given a key that is not on the tree", function() {
            expect(find(tree, 0)).toBeNull();
        });
    });

    describe("Least Common Ancestor", function() {
        it("should return the least common ancestor node", function() {
            expect(leastCommonAncestor(tree, 8, 24)).toBe(tree.root);
            expect(leastCommonAncestor(tree, 4, 24)).toBe(tree.root);
            expect(leastCommonAncestor(tree, 4, 12)).toBe(tree.root.left);
        });

        it("should return the least common ancestor even if the given nodes don' existst", function() {
            expect(leastCommonAncestor(tree, 1, 100)).toBe(tree.root);
            expect(leastCommonAncestor(tree, 4, 100)).toBe(tree.root);
            expect(leastCommonAncestor(tree, 7, 9)).toBe(tree.root.left);
        });

        it("should return node N if N.value is equal to one of the values and N is the parent of one of the node containing one of the other values", function() {
            expect(leastCommonAncestor(tree, 16, 8)).toBe(tree.root);
            expect(leastCommonAncestor(tree, 8, 4)).toBe(tree.root.left);
        });

        it("should return node N if N.value is equal to value1 and value2", function() {
            expect(leastCommonAncestor(tree, 16, 16)).toBe(tree.root);
            expect(leastCommonAncestor(tree, 8, 8)).toBe(tree.root.left);
        });
    });
});