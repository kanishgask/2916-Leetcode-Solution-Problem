class Solution {
    private static final long MOD = 1_000_000_007L;

    static class SegmentTree {
        long[] sum;
        long[] sqSum;
        long[] lazy;
        int n;

        SegmentTree(int n) {
            this.n = n;
            sum = new long[4 * n];
            sqSum = new long[4 * n];
            lazy = new long[4 * n];
        }

        private void apply(int node, int l, int r, long val) {
            long len = r - l + 1;

            sqSum[node] = (
                    sqSum[node]
                    + 2L * val % MOD * sum[node] % MOD
                    + len % MOD * val % MOD * val % MOD
            ) % MOD;

            sum[node] = (sum[node] + len % MOD * val) % MOD;

            lazy[node] = (lazy[node] + val) % MOD;
        }

        private void push(int node, int l, int r) {
            if (lazy[node] == 0) return;

            int mid = (l + r) >> 1;

            apply(node << 1, l, mid, lazy[node]);
            apply(node << 1 | 1, mid + 1, r, lazy[node]);

            lazy[node] = 0;
        }

        public void update(int ql, int qr, long val) {
            update(1, 0, n - 1, ql, qr, val);
        }

        private void update(int node, int l, int r,
                            int ql, int qr, long val) {

            if (ql > r || qr < l) return;

            if (ql <= l && r <= qr) {
                apply(node, l, r, val);
                return;
            }

            push(node, l, r);

            int mid = (l + r) >> 1;

            update(node << 1, l, mid, ql, qr, val);
            update(node << 1 | 1, mid + 1, r, ql, qr, val);

            sum[node] = (sum[node << 1] + sum[node << 1 | 1]) % MOD;
            sqSum[node] = (sqSum[node << 1] + sqSum[node << 1 | 1]) % MOD;
        }

        long totalSquareSum() {
            return sqSum[1];
        }
    }

    public int sumCounts(int[] nums) {
        int n = nums.length;

        SegmentTree st = new SegmentTree(n);

        java.util.Map<Integer, Integer> lastPos = new java.util.HashMap<>();

        long ans = 0;

        for (int i = 0; i < n; i++) {
            int prev = lastPos.getOrDefault(nums[i], -1);

            st.update(prev + 1, i, 1);

            ans = (ans + st.totalSquareSum()) % MOD;

            lastPos.put(nums[i], i);
        }

        return (int) ans;
    }
}
