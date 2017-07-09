import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { TestCase } from './test-case.model';
import { TestCaseService } from './test-case.service';

@Component({
    selector: 'jhi-test-case-detail',
    templateUrl: './test-case-detail.component.html'
})
export class TestCaseDetailComponent implements OnInit, OnDestroy {

    testCase: TestCase;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private testCaseService: TestCaseService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInTestCases();
    }

    load(id) {
        this.testCaseService.find(id).subscribe((testCase) => {
            this.testCase = testCase;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInTestCases() {
        this.eventSubscriber = this.eventManager.subscribe(
            'testCaseListModification',
            (response) => this.load(this.testCase.id)
        );
    }
}
