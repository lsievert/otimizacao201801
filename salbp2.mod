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

#param p{i in N, j in N}, default 0;
set P, within N cross N;
/* precedence of tasks i,j */

var x{s in S, i in N}, binary;
/* 1 if task i ∈ N is assigned to station s ∈ S, 0 otherwise */

minimize cycle: sum{s in S, i in N} t[i] * x[s,i];

#s.t. cycle{s in S}: sum{i in N} t[i] * x[s,i] <= c;
#/* cycle per station */

s.t. occurence{i in N}: sum{s in S} x[s,i] = 1;
/* satisfy occurence */

#s.t. precedence{i in N, j in N: j > i, t in S}: x[t,j] <= sum{s in S: s <= t} x[s,i];
#/* satisfy precedence: N: j > i <=> Following set of task i */

#s.t. precedence{i in N, j in N: j > i}: sum{s in S} s * x[s,i] <= sum{s in S} s * x[s,j];
#/* satisfy precedence: N: j > i <=> Following set of task i */

solve;
display cycle;

end;

