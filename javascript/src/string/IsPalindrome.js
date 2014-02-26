var isPalindrome = function(string) {
    for(var i = 0; i < string.length/2; i++) {
        if(string.charAt(i) !== string.charAt(string.length - i - 1)) {
            return false;
        }
    }
    return true;
}

describe("String/IsPalindrome", function() {
    it("One letter words are palindromes", function() {
        expect(isPalindrome('a')).toBe(true);
        expect(isPalindrome('b')).toBe(true);
    });

    it("Empty strings are palindromes", function() {
        expect(isPalindrome('')).toBe(true);
        expect(isPalindrome('')).toBe(true);
    });

    it("Palindromes are palindromes", function() {
        expect(isPalindrome('aba')).toBe(true);
        expect(isPalindrome('arara')).toBe(true);
        expect(isPalindrome('aabbaa')).toBe(true);
        expect(isPalindrome('aa')).toBe(true);
    });

    it("Non palindromes are not palindromes", function() {
        expect(isPalindrome('abc')).toBe(false);
        expect(isPalindrome('ab')).toBe(false);
        expect(isPalindrome('I want some coffee!')).toBe(false);
    });

    it("Case matters", function() {
        expect(isPalindrome('aBa')).toBe(true);
        expect(isPalindrome('aA')).toBe(false);
        expect(isPalindrome('araRA')).toBe(false);
    });
});
