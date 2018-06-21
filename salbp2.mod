param n, integer, > 0;
/* number of tasks */

param m, integer, > 0;
/* number of stations */

set N := 1..n;
/* tasks */

set S := 1..m;
/* stations */

param t{w in N};
/* time of task i */

set P{j in N}, within N, default {};
/* precedence of tasks i,j */

var x{s in S, i in N}, binary;
/* 1 if task i ∈ N is assigned to station s ∈ S, 0 otherwise */

var c;

s.t. cycles{s in S}: sum{i in N} t[i] * x[s,i] <= c;
/* cycle per station */

s.t. occurence{i in N}: sum{s in S} x[s,i] = 1;
/* satisfy occurence */

s.t. precedence{i in N, j in P[i], k in S}: x[k,j] <= sum{s in S: s <= k} x[s,i];
/* satisfy precedence: P[i] => Following tasks of task i */

minimize obj: c;

solve;
display obj;

end;

