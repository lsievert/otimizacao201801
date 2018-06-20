param m;
/* number of stations */

set N;
/* tasks */

set S := 1..m;
/* stations */

param t{w in N};
/* time of task i */

param p{i in N, j in N};
/* precedence of tasks i,j */

var x{s in S, i in N};
/* 1 if task i ∈ N is assigned to station s ∈ S, 0 otherwise */

minimize max_cycle: c;

s.t. cycle{s in S}: sum{i in N} t[i] * x[s,i] <= c;
/* cycle per station */

s.t. occurence{i in N}: sum{s in S} x[s,i] = 1;
/* satisfy occurence */


s.t. precedence{i in N, j in N: j > i}: sum{s in S} s * x[s,i] <= sum{s in S} s * x[s,j];
/* satisfy precedence: N: j > i <=> Following set of task i */


end;

