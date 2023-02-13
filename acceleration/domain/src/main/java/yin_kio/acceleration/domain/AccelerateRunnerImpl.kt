package yin_kio.acceleration.domain

class AccelerateRunnerImpl(
    private val outer: Outer
) : AccelerateRunner {

    override fun run(){
        outer.showAccelerateProgress()
        outer.showInter()
        outer.complete()
    }

}