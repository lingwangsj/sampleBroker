/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { Headers } from '@angular/http';

import { SampleBrokerTestModule } from '../../../test.module';
import { DomainMySuffixComponent } from '../../../../../../main/webapp/app/entities/domain-my-suffix/domain-my-suffix.component';
import { DomainMySuffixService } from '../../../../../../main/webapp/app/entities/domain-my-suffix/domain-my-suffix.service';
import { DomainMySuffix } from '../../../../../../main/webapp/app/entities/domain-my-suffix/domain-my-suffix.model';

describe('Component Tests', () => {

    describe('DomainMySuffix Management Component', () => {
        let comp: DomainMySuffixComponent;
        let fixture: ComponentFixture<DomainMySuffixComponent>;
        let service: DomainMySuffixService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SampleBrokerTestModule],
                declarations: [DomainMySuffixComponent],
                providers: [
                    DomainMySuffixService
                ]
            })
            .overrideTemplate(DomainMySuffixComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(DomainMySuffixComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DomainMySuffixService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new Headers();
                headers.append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of({
                    json: [new DomainMySuffix(123)],
                    headers
                }));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.domains[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
